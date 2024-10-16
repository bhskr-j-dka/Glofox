-- Create table for 'class'
CREATE TABLE IF NOT EXISTS class (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    capacity INT NOT NULL
);

-- Create table for 'booking'
CREATE TABLE IF NOT EXISTS booking (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    date DATE NOT NULL,
    class_id INT,
    FOREIGN KEY (class_id) REFERENCES class(id)
);

-- Insert classes into the 'class' table
INSERT INTO class (name, start_date, end_date, capacity) VALUES 
('Pilates', '2000-01-01', '2002-01-01', 10),
('Yoga', '2003-01-01', '2005-01-01', 15),
('Zumba', '2006-01-01', '2008-01-01', 20);


-- Insert bookings into the 'booking' table
INSERT INTO booking (name, date, class_id) VALUES
('John Doe', '2001-01-01', 1),
('Jane Smith', '2004-01-10', 2),
('Jack Wilson', '2007-01-15', 3);