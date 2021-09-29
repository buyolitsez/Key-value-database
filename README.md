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

Or if you want to do the single operation you can run the program in terminal with operation in run args

For example

```./gradlew run --args="containsKey KEY"``` or

```./gradlew run --args="set KEY VALUE"``` or 

```./gradlew run --args="clear"```

---

## List of options

+ ```exit``` - exit from database interface
+ ```containsKey [KEY]``` - checks if the database contains the given key(true or false).
+ ```get [KEY]``` - output ```Can't find key``` if key doesn't exist, or the value of key.
+ ```set [KEY] [VALUE]``` - set the value of KEY to VALUE.
+ ```removeKey [KEY]``` - Removes the specified KEY and its corresponding value from this map.
+ ```size``` - output size of database.
+ ```is-empty``` - output 1 or 0, is database empty or not respectively.
+ ```clear``` - removes all elements from database.
+ ```values``` - output all values in database.
---
