# Курс основ программирования на МКН СПбГУ
## Проект 2: key-value база данных

[Постановка задачи](./TASK.md)

# *User guide*  

---

## Using

You can run the program with IDE

Then you should write commands

For example

```containsKey KEY``` or

```set KEY VALUE``` or

```size```

where KEY and VALUE is a strings

Or if you can do operations by writing it in run args

For example

```./gradlew run --args="containsKey KEY"``` or

```./gradlew run --args="set KEY VALUE containsKey KEY VALUE"``` or 

```./gradlew run --args="size values clear"```

---

## List of options

You can see some variants for function names, you can use one, that you prefer

+ ```exit``` - exit from database interface

* ```containsKey [KEY]``` - checks if the database contains the given key(true or false).
* ```find``` 

+ ```get [KEY]``` - output ```Can't find key``` if key doesn't exist, or the value of key.

* ```set [KEY] [VALUE]``` - set the value of KEY to VALUE.
* ```change```

+ ```removeKey [KEY]``` - Removes the specified KEY and its corresponding value from this map.
+ ```remove```
+ ```delete```

* ```size``` - output size of database.
* ```len```
* ```length```

+ ```is-empty``` - output 1 or 0, is database empty or not respectively.
+ ```isEmpty```
+ ```empty```

* ```clear``` - removes all elements from database.
* ```flush```

+ ```values``` - output all values in database.
+ ```value```
---

## Addition

You can change default values in file [settings](settings)

---
