package org.fuwjin.jon;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.fuwjin.chessur.Catalog;
import org.fuwjin.chessur.CatalogManagerImpl;
import org.fuwjin.chessur.Script;
import org.fuwjin.dinah.Adapter.AdaptException;
import org.fuwjin.dinah.Function;
import org.fuwjin.dinah.FunctionProvider;
import org.fuwjin.dinah.FunctionProvider.NoSuchFunctionException;

public class Registry {
   private static CatalogManagerImpl manager = new CatalogManagerImpl();
   private static Catalog jonParser;

   private static Script parser() throws ExecutionException, IOException {
      if(jonParser == null) {
         jonParser = manager.loadCat("org/fuwjin/jon/JonParser.cat");
      }
      return jonParser;
   }

   private final FunctionProvider provider;
   private final Map<String, Object> values = new HashMap<String, Object>();
   private final Map<Object, Container> containers = new IdentityHashMap<Object, Container>();
   private int count = 0;

   public Registry() {
      this(manager);
   }

   public Registry(final FunctionProvider provider) {
      this.provider = provider;
   }

   public Object adapt(final Object value, final Type type) throws AdaptException {
      final Container container = containers.get(value);
      if(container != null) {
         container.adapt(provider, type);
         return container;
      }
      return manager.adapt(value, type);
   }

   public void add(final Collection c, final Object value) throws Exception {
      if(value instanceof Reference) {
         container(c).add(value);
      } else {
         c.add(value);
      }
   }

   public Function getFunction(final Type type, final String name, final int count) throws NoSuchFunctionException {
      try {
         return provider.forCategoryName(type, name).withArgCount(count).function();
      } catch(final AdaptException e) {
         throw new NoSuchFunctionException(e, "could not produce name for %s", type);
      }
   }

   public Function getFunction(final Type type, final String name, final Type... parameters)
         throws NoSuchFunctionException {
      try {
         return provider.forCategoryName(type, name).withTypedArgs(parameters).function();
      } catch(final AdaptException e) {
         throw new NoSuchFunctionException(e, "could not produce name for %s", type);
      }
   }

   public boolean hasUnresolvedReferences() {
      return count > 0;
   }

   public Object load(final Reader input) throws Exception {
      return load(input, null);
   }

   public Object load(final Reader input, final Type type) throws Exception {
      final Map<String, Object> env = new HashMap<String, Object>();
      env.put("registry", this);
      env.put("null", null);
      env.put("true", true);
      env.put("false", false);
      env.put("type", type);
      final Object value = parser().acceptFrom(input).publishTo(System.out).withState(env).exec();
      if(value instanceof Reference) {
         return ((Reference)value).value();
      }
      return value;
   }

   public void put(final Map map, final Object key, final Object value) throws Exception {
      if(key instanceof Reference || value instanceof Reference) {
         container(map).put(key, value);
      } else {
         map.put(key, value);
      }
   }

   public Object retrieve(final String name) {
      Object value = values.get(name);
      if(value == null) {
         value = new Reference();
         values.put(name, value);
         count++;
      }
      return value;
   }

   public void set(final Object target, final Function setter, final Object value) throws Exception,
         InvocationTargetException {
      if(value instanceof Reference) {
         container(target).set(setter, value);
      } else {
         setter.invoke(target, value);
      }
   }

   public void store(final String name, final Object value) throws Exception {
      final Reference ref = (Reference)values.get(name);
      if(ref != null) {
         ref.resolve(value);
         count--;
      }
      values.put(name, value);
   }

   private Container container(final Object c) {
      Container container = containers.get(c);
      if(container == null) {
         container = new Container(c);
         containers.put(c, container);
      }
      return container;
   }
}
