/*
Сделать запрос для получения атрибутов из указанных таблиц, применив фильтры по указанным условиям:
Таблицы: Н_ЛЮДИ, Н_ВЕДОМОСТИ, Н_СЕССИЯ.
Вывести атрибуты: Н_ЛЮДИ.ОТЧЕСТВО, Н_ВЕДОМОСТИ.ЧЛВК_ИД, Н_СЕССИЯ.ДАТА.
Фильтры (AND):
a) Н_ЛЮДИ.ИД > 100865.
b) Н_ВЕДОМОСТИ.ЧЛВК_ИД < 163249.
Вид соединения: INNER JOIN.
*/

SET enable_nestloop = false;
SET enable_hashjoin = true;

SELECT COUNT(ИД) FROM Н_ЛЮДИ;
SELECT COUNT(DISTINCT ЧЛВК_ИД) FROM Н_ВЕДОМОСТИ;
SELECT COUNT(DISTINCT ЧЛВК_ИД) FROM Н_СЕССИЯ;

# EXPLAIN (analyze, costs off, timing off, summary off) 
EXPLAIN analyze SELECT 
	Н_ЛЮДИ.ОТЧЕСТВО, 
    Н_ВЕДОМОСТИ.ЧЛВК_ИД, 
    Н_СЕССИЯ.ДАТА
FROM Н_ЛЮДИ
INNER JOIN Н_ВЕДОМОСТИ ON Н_ЛЮДИ.ИД = Н_ВЕДОМОСТИ.ЧЛВК_ИД
INNER JOIN Н_СЕССИЯ ON Н_ЛЮДИ.ИД = Н_СЕССИЯ.ЧЛВК_ИД
WHERE Н_ЛЮДИ.ИД > 100865 
    AND Н_ВЕДОМОСТИ.ЧЛВК_ИД < 163249;

CREATE INDEX ИНД_СЕССИЯ_ЧЛВК_ИД ON Н_СЕССИЯ USING BTREE("ЧЛВК_ИД");
