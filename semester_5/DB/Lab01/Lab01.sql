CREATE SCHEMA IF NOT EXISTS s335105;

CREATE TABLE IF NOT EXISTS s335105.Ships (
    ship_id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY(ship_id)
);

CREATE TABLE IF NOT EXISTS s335105.Employees (
    employee_id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) NOT NULL,
    job_title VARCHAR(50) NOT NULL,
    ship_id INT NOT NULL
        REFERENCES s335105.Ships(ship_id)
        ON DELETE CASCADE,
    PRIMARY KEY(employee_id)
);
CREATE TABLE IF NOT EXISTS s335105.Skills (
    skill_id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    PRIMARY KEY(skill_id)
);
CREATE TABLE IF NOT EXISTS s335105.DangerLevel (
    danger_id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) NOT NULL,
    level INT NOT NULL,
    PRIMARY KEY(danger_id)
);
CREATE TABLE IF NOT EXISTS s335105.Problems (
    problem_id INT GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    danger_id INT NOT NULL 
        REFERENCES s335105.DangerLevel(danger_id) 
        ON DELETE SET NULL,
    PRIMARY KEY(problem_id)
);

CREATE TABLE IF NOT EXISTS s335105.EmployeeSkills (
    employee_id INT NOT NULL 
        REFERENCES s335105.Employees(employee_id) 
        ON DELETE CASCADE,
    skill_id INT NOT NULL 
        REFERENCES s335105.Skills(skill_id) 
        ON DELETE CASCADE    
);

CREATE TABLE IF NOT EXISTS s335105.SkillsToFixProblem (
    skill_id INT NOT NULL 
        REFERENCES s335105.Skills(skill_id) 
        ON DELETE CASCADE,
    problem_id INT NOT NULL 
        REFERENCES s335105.Problems(problem_id) 
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS s335105.ShipProblems (
    problem_id INT NOT NULL 
        REFERENCES s335105.Problems(problem_id) 
        ON DELETE CASCADE,
    ship_id INT NOT NULL 
        REFERENCES s335105.Ships(ship_id) 
        ON DELETE CASCADE    
);
