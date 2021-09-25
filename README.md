# Курс основ программирования на МКН СПбГУ
## Проект 2: key-value база данных

[Постановка задачи](./TASK.md)

# *User guide*  

---

## Using

You can run the program with the command <br>
``` #bash
./gradlew run --args="[OPTIONS]"
```

For example ```./gradlew run --args="find |YOUR KEY"``` or

```./gradlew run --args="change |KEY|VALUE"``` or

```./gradlew run --args="size"```


---

## List of options

+ ```contains [KEY]``` - checks if the database contains the given key(true or false).
+ ```get [KEY]``` - output ```Can't find key``` if key doesn't exist, or the value of key.
+ ```set [KEY] [VALUE]``` - set the value of KEY to VALUE.
+ ```remove [KEY]``` - Removes the specified KEY and its corresponding value from this map.
+ ```size``` - output size of database.
+ ```is-empty``` - output 1 or 0, is database empty or not respectively.
+ ```clear``` - removes all elements from database.
+ ```entries``` - output all key/value pairs in database.
+ ```keys``` - output all keys in database.
+ ```values``` - output all values in database.
---
