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

## DESCRIPTION

    

---

## List of options

+ ```find "[KEY]"``` - output ```Can't find key``` if key doesn't exist, or the value of key.
+ ```change "[KEY]" "[VALUE]"``` - set the value of KEY to VALUE.
+ ```delete "[KEY]"``` - delete cell with KEY.
+ ```print``` - output all database in random order.
+ ```size``` - output size of database.
+ ```is-empty``` - output 1 or 0, is database empty or not respectively.
+ ```clear``` - reset all data
+ ```rehash``` - recalculate hash values of database.
---
