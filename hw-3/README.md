# Linear Regression on  Scala

- Принимает на вход csv файлы для трейна и теста. Тренировочные датасеты в файлах [boston-train.csv](boston-train.csv) и [boston-test.csv](boston-test.csv) соответственно
- Результаты лежат в файлах [out_train.csv](out_train.csv) и [out_test.csv](out_test.csv)
- Процесс обучения по эпохам в файле [linear_regression.log](linear_regression.log)
- Сперва делается CV (с нормировкой по "обучающей" части), потом делается обычное обучение (здесь тоже нормируется трейн) и предикт на тесте (тест нормируется по статистикам с трейна).

Запускать с параметрами:  
`--data-train boston-train.csv --data-test boston-test.csv --target-column 13 --out-train out_train.csv --out-test out_test.csv`
