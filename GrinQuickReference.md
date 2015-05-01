# Grin Quick Reference #

## Conventions ##
In the summary below, the following conventions are followed:
  * **_`Ident`_** is a dotted identifier, e.g. **`some.qualified.identifier`** is a valid **_`Ident`_**
  * **_`Name`_** is a colon separated list of one or more **_`Ident`_**s, e.g. **`some.qualified.name:other.name:ident`** is a valid **_`Name`_**. Any of the **_`Ident`_**s may be followed by open-close square braces except for the last one, for instance **`some.class[][]:bob[]:hope`** is a valid **_`Name`_**.
  * **_`Type`_** is a **_`Name`_** which may be followed by open-close square braces, e.g. **`some.class[][]:bob[]:hope[]`** is a valid **_`Type`_**.
  * **_`Signature`_** is a **_`Name`_** followed by a list of zero or more **_`Type`_**s in parentheses, e.g. **`some.class:method(int,core:String)`** would be a valid **_`Signature`_**.
  * **_`Key`_** is a **_`Value`_**, it is used here to distinguish between key-values and value-values in an array.
  * **_`Array`_** is a special **_`Value`_** which provides indexed or keyed lookups into a collection of values.
  * **_`Source`_** and **_`Sink`_** are **_`Name`_**s used to more clearly describe what happens during redirection.
  * **_`Char`_** is a literal character (x), an escaped character (\x), a unicode escape (\u1234), or an extended unicode escape (\x123456). Not all characters can be escaped, see the individual notes for literals for more on safe escapes.
  * **_`Literal`_** is a sequence of zero or more **_`Char`_**s. Single quotes (') and backslashes (\) must always be escaped (\' and \\ respectively). Double quotes (") must be escaped (\") in dynamic literals, but may always be safely escaped. New lines (\n), tabs (\t), and carriage returns (\r) may be escaped (as shown), however this is not required as multi-line literals are allowed.
  * **_`Range`_** is a literal which is either a single **_`Char`_** (a) or a pair of **_`Char`_**s separated by a hyphen (a-z) indicating all characters between and including the **_`Char`_**s. Hyphens (-) must be escaped (\-) unless they are the first character of the first range (`[-]` or `[--a]`), or the second character of a range (`[a--]`) and may always be safely escaped. Close square brackets (]) must be escaped (\]) when used as the first character of a range (`[\]]` or `[\]-a]`) and may always be safely escaped. New lines (\n), tabs (\t), and carriage returns (\r) must be escaped (as shown).
  * **_`Number`_** is a literal numerical expression allowing for integral and floating points.
  * **_`Integer`_** is a **_`Number`_** restricted to just an integral expression.
  * **_`NumericalRange`_** is either a single **_`Number`_** (10) or a pair of **_`Number`_**s separated by a double dot (1..10) indicating all numbers between and including the **_`Number`_**s.

## Declarations ##
  * **`alias `_`Name`_` as `_`Name`_**: Aliases the first name as the second name. The second name may be used as a substitute for the first name in the rest of the catalog.
  * **`alias `_`Signature`_` as `_`Name`_**: Aliases a signature as a name. The name may be used to explicitly identify the function matching the signature instead of relying on name-only resolution.
  * **_`Variable`_` = `_`Value`_**: Assigns a value to a variable outside of a script context. This value will be resolved once and shared across all script executions.
  * **`function `_`Name`_` ( `_`Ident`_`, `_`Ident`_`, ... ) { `_`Statement`_` `_`Statement`_` ... }`**: Creates a function named Name which will assign arguments to the Ident parameters before executing the Statements in order.
  * **`<`_`Name`_`> { `_`Statement`_` `_`Statement`_` ... }`**: Creates a script named Name which will execute Statements in order.

## Statements ##
  * **`assuming `_`Statement`_**: asserts that the Statement would succeed if executed.
    * succeeds if: Statement would succeed if executed
    * fails if: Statement would fail if executed
  * **`unless `_`Statement`_**: asserts that the Statement would fail if executed.
    * succeeds if: Statement would fail if executed
    * fails if: Statement would succeed if executed
  * **`assuming `_`Value`_**: asserts that the Value is not a default value.
    * succeeds if: Value is a non-default value
    * fails if: Value is unset or default
  * **`unless `_`Value`_**: asserts that the Value is unset or a default value.
    * succeeds if: Value is unset or a default value
    * fails if: Value is a non-default value
  * **`assuming `_`Value`_` in `_`Array`_**: asserts that the Value is in the array of values.
    * succeeds if: Value is in the array
    * fails if: Value is unset or not in the array
  * **`unless `_`Value`_` in `_`Array`_**: asserts that the Value is unset or not in the array of values.
    * succeeds if: Value is unset or not in the array
    * fails if: Value is in the array
  * **`either `_`Statement`_` or `_`Statement`_` or ...`**: attempts to execute Statements until one succeeds. Or clauses may be added as needed.
    * succeeds if: the first Statement succeeds, otherwise if the second Statement succeeds, otherwise...
    * fails if: all statements fail
  * **`could `_`Statement`_**: attempts to execute the Statement, but continues even if it fails.
    * succeeds if: always succeeds
    * fails if: never fails
  * **`repeat `_`Statement`_**: repeatedly executes the Statement until it fails.
    * succeeds if: Statement executes at least once
    * fails if: Statement fails on the first execution
  * **`accept `_`Value`_**: Consumes the Value from the input.
    * succeeds if: the input exactly matches the Value
    * fails if: the input does not have enough characters, or the input does not exactly match the Value
  * **`accept unless `_`Value`_**: Consumes the next character from the input, if the input does not match the Value.
    * succeeds if: the input does not exactly match the Value, or there are insufficient character to match
    * fails if: the input exactly matches the Value
  * **`accept until `_`Value`_**: Consumes characters from the input until the input matches the Value.
    * succeeds if: at least one character from the input does not exactly match the Value, and the Value is eventually found in the input
    * fails if: the input has no more characters, or if the value is the next input, or if the input does not contain the value
  * **`accept through `_`Value`_**: Consumes characters from the input until the input matches the Value, then consumes the value.
    * succeeds if: the value is eventually found in the input
    * fails if: the input has no more characters, or if the value is not found in the input
  * **`accept in `_`Array`_**: Consumes characters from the input matching a value in the array.
    * succeeds if: the input matches one of the values in the array
    * fails if: the input has no more characters, or no value in the array matches the input
  * **`accept unless in `_`Array`_**: Consumes the next character from the input if no value in the array matches the input.
    * succeeds if: no value in the array matches the input
    * fails if: the input has no more characters, or the next input matches an array value
  * **`accept until in `_`Array`_**: Consumes characters from the input until the input matches a value in the array.
    * succeeds if: if at least one character from the input is consumed and the input eventually matches one of the values in the array
    * fails if: the input has no more characters, or if a value from the array is next in the input, or no value in the array ever matches in the input
  * **`accept through in `_`Array`_**: Consumes characters from the input until the input matches a value in the array, then consumes the value in the array.
    * succeeds if: the input eventually matches one of the values in the array
    * fails if: the input has no more characters, or no value in the array ever matches the input
  * **`publish `_`Value`_**: Serializes the Value to the output stream. Since the output may be buffered, errors in serializing the Value to the output stream may result in aborted execution rather than execution failure.
    * succeeds if: Value is set
    * fails if: Value is unset
  * **`log `_`Value`_**: Serializes the Value immediately to the logging stream.
    * succeeds if: Value is set
    * fails if: Value is unset
  * **`return `_`Value`_**: Returns a Value from a script. Must be the last statement of a script declaration block.
    * succeeds if: Value is set
    * fails if: Value is unset
  * **`abort `_`Value`_**: Aborts execution with a message of Value. If the Value is unset, then a generic message will be used instead.
    * succeeds if: never succeeds
    * fails if: never fails
  * **`<`_`Name`_`>`**: Executes a script named Name with the same input, output and environment as the current script.
    * succeeds if: the script named Name succeeds
    * fails if: the script named Name fails
  * **`<`_`Name`_`> << `_`Value`_**: Redirects input for the script named Name to the serialized Value. Uses the same output and environment as the current script.
    * succeeds if: Value is set and the script named Name succeeds
    * fails if: Value is unset or the script named Name fails
  * **`<`_`Sink`_`> << <`_`Source`_`>`**: Redirects the output for the Source script to the input for the Sink script. The current environment is used for both scripts, as is the current input for the Source script, and the current output for the Sink script.
    * succeeds if: Source and Sink both succeed
    * fails if: either Source or Sink fail
  * **_`Variable`_` << <`_`Source`_`>`**: Redirects the output from Source to a variable.
    * succeeds if: Source succeeds
    * fails if: Source fails
  * **`{ `_`Statement`_` `_`Statement`_` ... }`**: executes a block of Statements in order.
    * succeeds if: all Statements succeed
    * fails if: any Statement fails
  * **_`Variable`_` = `_`Value`_**: assigns the Value to the variable.
    * succeeds if: Value is set
    * fails if: Value is unset
  * **_`Name`_` ( `_`Value`_`, `_`Value`_`, ... )`**: invokes the function named Name with the Values as arguments.
    * succeeds if: the invocation succeeds
    * fails if: the Values cannot be adapted to appropriate types, or if the invoked function fails.

## Variables ##
  * **_`Ident`_**: map a value to an instance variable.
    * can assign if: ident is not a reserved word
    * fails assign if: ident is a reserved word
  * **_`Ident`_`[`_`Key`_`]`**: map a value to a key in an array.
    * can assign if: the key is valid for the given array
    * fails assign if: the key is not a valid type for the given array, or the variable Ident is not an array
  * **_`Ident`_`[`_`Key`_`?]`**: map if absent. Only assigns the value to the key if the key has not already been set
    * can assign if: the key is valid for the given array and has not been mapped to a value yet
    * fails assign if: the key is not a valid type for the given array, or the variable Ident is not an array
  * **_`Ident`_`[`_`Key`_`!]`**: absent and map. Demands that the key has not been mapped to a value yet, or it triggers a failure
    * can assign if: the key is valid for the given array and has not been mapped to a value yet
    * fails assign if: the key is not a valid type for the given array, or if the key has already been mapped, or the variable Ident is not an array
  * **_`Ident`_`[`_`Key`_`+]`**: insert. Inserts the value after the key, only available on indexed arrays
    * can assign if: the key is valid for the given array and has not been mapped to a value yet
    * fails assign if: the key is not a valid type for the given array, or if the key has already been mapped, or the variable Ident is not an array
  * **_`Ident`_`[+]`**: Add. Adds the value to the end of the array, only available on indexed arrays
    * can assign if: the given array is indexed
    * fails assign if: the variable Ident is not an array

## Values ##

  * **`'`_`Literal`_`'`**: static string literal
    * set if: always set
    * unset if: never unset
  * **`"`_`Literal`_`'`_`Value`_`'`_`Literal`_`..."`**: dynamic string literal. Characters between single quotes are interpreted as Values.
    * set if: all inner Values are set
    * unset if: any inner Values are unset
  * **_`Number`_**: a numeric literal.
    * set if: always set
    * unset if: never unset
  * **`[]`**: an empty indexed array.
    * set if: always set
    * unset if: never unset
  * **`[=]`**: an empty associative array.
    * set if: always set
    * unset if: never unset
  * **`[`_`Value`_`, `_`Value`_`, ... ]`**: an indexed array literal. Values in an indexed array are keyed by their integral position in the array.
    * set if: the values are all set
    * unset if: any of the values are unset
  * **`[`_`Key`_` = `_`Value`_`, `_`Key`_` = `_`Value`_`, ... ]`**: an associative array literal.
    * set if: the keys and values are all set
    * unset if: any of the keys or values are unset
  * **`[`_`RangeRangeRange`_`]`**: a character array literal. Ranges are not delimited. Ranges must be unique, in particular, single characters must not repeat. Character arrays have very high precedence, so `[name]` will be interpreted as a character array of n, a, m, and e. Note however that `[ name ]` will not be interpreted as a character array because of the two spaces.
    * set if: always set
    * unset if: never unset
  * **`[`_`NumericalRange`_`, `_`NumericalRange`_`, ... ]`**: a numeric array literal.
    * set if: always set
    * unset if: never unset
  * **`(`_`Type`_`) [ `_`Name`_` = `_`Value`_`, ... ]`**: an object literal.
    * set if: an object can be created of the Type, and all the Values can be adapted to the Field type
    * unset if: the Type cannot be instantiated, or if any Value cannot be adapted to the Field type
  * **`input:match`**: returns the input consumed during the current script execution.
    * set if: always set, possibly empty if no input was consumed
    * unset if: never unset
  * **`input:next`**: returns the unicode codepoint of the next character in the input stream.
    * set if: there is another character in the input stream
    * unset if: the input is at the end of the stream
  * **`input:line`**: returns the total number of newline separated sequences consumed from the input.
    * set if: always set, starts at 1
    * unset if: never unset
  * **`input:column`**: returns the number of characters consumed from the input since the last newline.
    * set if: always set, starts at 0
    * unset if: never unset
  * **`output:line`**: returns the total number of newline separated sequences published to the output.
    * set if: always set, starts at 1
    * unset if: never unset
  * **`output:column`**: returns the number of characters published to the output since the last newline.
    * set if: always set, starts at 0
    * unset if: never unset
  * **_`Name`_`[`_`Key`_`]`**: returns the value mapped to key in the array.
    * set if: the key has been mapped to a value, and the name is set to an array
    * unset if: the key has not been mapped or the name is unset or not set to an array
  * **_`Name`_`[`_`Key`_`-]`**: remove. returns the value mapped to key in the array, and unmaps the key.
    * set if: the key has been mapped to a value, and the name is set to an array
    * unset if: the key has not been mapped or the name is unset or not set to an array
  * **_`Name`_`[#]`**: size. returns the size of the array.
    * set if: the name is set to an array
    * unset if: the name is unset or not set to an array
  * **_`Name`_`[`_`Key`_`?]`**: contains. returns true if the key has been mapped in the array, false otherwise.
    * set if: the name is set to an array
    * unset if: the name is unset or not set to an array
  * **_`Name`_`[?]`**: non-empty. returns true if the array contains any mapped keys, false otherwise.
    * set if: the name is set to an array
    * unset if: the name is unset or not set to an array
  * **_`Name`_`[!]`**: is array. fails if not an array.
    * set if: the name is set to an array
    * unset if: the name is unset or not set to an array
  * **_`Name`_`[>>]`**: iterate. returns the next key in the array. This instruction is safe with respect to mutation, but may not visit all keys if the array is changed while iterating.
    * set if: the name is set to an array and there are more keys to visit
    * unset if: the name is unset or not set to an array, or the keys have all been visited
  * **_`Name`_`[<<]`**: reverse iterate. returns the previous key in the indexed array, not allowed with associative arrays. This instruction is safe with respect to mutation, but may not visit all keys if the array is changed while iterating.
    * set if: the name is set to an array and there are more keys to visit
    * unset if: the name is unset or not set to an array, or the keys have all been visited
  * **_`Name`_`[-]`**: tail remove. removes and returns the last value in the indexed array, not allowed with associative arrays.
    * set if: the name is set to an array and the array is not empty
    * unset if: the name is unset or not set to an array, or the array is empty
  * **`<`_`Name`_`>`**: The script named Name is executed and its return value is used as the value.
    * set if: the script executes successfully and returns a set value
    * unset if: the script fails or does not have a return statement
  * **_`Name`_` ( `_`Value`_`, `_`Value`_`, ... )`**: invokes the function named Name with the Values as arguments, and returns the return value of the invocation.
    * set if: the invocation succeeds and returns a value
    * unset if: the Values cannot be adapted to appropriate types, or if the invoked function fails, or if the function has a void return type
  * **_`Name`_**: a variable value: returns the value of the variable in the current environment.
    * set if: the variable has been set to a value
    * unset if: the variable has not been set to a value