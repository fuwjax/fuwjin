<Element>{
  either{
    accept '&'
    name = <Name>
    either {
      accept '=' <S>
      value = <Value>
      Registry.store(registry, name, value)
    } or {
      value = Registry.retrieve(registry, name)
    }
  } or {
    value = <Value>
  }
  return value
}
<Value>{
  could type = <ClassCast>
  either{
    assume not type
    value = <UnknownValue>
  } or value = <KnownValue>
  return value
}
<ClassCast>{
  accept '(' <S>
  either {
    accept '&'
    name = <Name>
    either{
      accept '=' <S>
      value = <Class>
      Registry.store(registry, name, value)
    } or {
      value = Registry.retrieve(registry, name)
    }
  } or {
    value = <Class>
  }
  #check that value is assignable to type?
  accept ')' <S>
  return value
}
<UnknownValue>{
  either value=<UnknownMap>
  or value = <UnknownList>
  or value = <String>
  or value = <Literal>
  or value = <UnknownNumber>
  return value
}
<KnownValue>{
  either value = <Object>
  or value = <KnownMap>
  or value = <KnownCollection>
  or value = <Array>
  or value = <Class>
  or value = <Enum>
  or value = <KnownString>
  or value = <Null>
  or value = <Boolean>
  or value = <Float>
  or value = <Double>
  or value = <Long>
  or value = <Integer>
  or value = <Short>
  or value = <Byte>
  or value = <Character>
  return value
}
<Primitive>{
  either value = <String>
  or value = <Null>
  or value = <Boolean>
  or value = <Number>
  return value
}
<Number> {
  either value = <Float>
  or value = <Double>
  or value = <Long>
  or value = <Integer>
}```