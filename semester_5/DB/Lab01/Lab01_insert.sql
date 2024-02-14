INSERT INTO s335105.ships (name) VALUES ('Дискавери');

INSERT INTO s335105.employees (name, job_title, ship_id) VALUES ('Фрэнк Пул', 'Инжинер', 1);

INSERT INTO s335105.skills (name, description) VALUES 
    ('Замена блока', 'Замена повреждённого блока космического корабля на новый'),
    ('Проверка состояния оболочки', 'Визуальная проверка состояния оболочки корабля на наличие видимых деффектов, например, микропробоин'),
    ('Заделка микропробоин', 'Ремонт небольших микропробоин на внешней оболчке корабля');

INSERT INTO s335105.dangerlevel (name, level) VALUES 
    ('Незначительный', 1),
    ('Низкий', 2),
    ('Средний', 3),
    ('Высокий', 4),
    ('Критический', 10);

INSERT INTO s335105.problems (name, description, danger_id) VALUES 
    ('Повреждённый блок', 'Важнейший элемент корабля блок что-то повреждён и без него корабль не сможет нормально функционировать', 5),
    ('Микропробоины на оболочке', 'Небольшие микропробоины на оболочке, которые могут быть опасны лишь в очень большой перспективе', 1);

INSERT INTO s335105.employeeskills (employee_id, skill_id) VALUES (1, 1), (1, 2), (1, 3);

INSERT INTO s335105.skillstofixproblem (skill_id, problem_id) VALUES (1, 1), (2, 2), (3, 2);

INSERT INTO s335105.shipproblems (ship_id, problem_id) VALUES (1, 1), (1, 2);