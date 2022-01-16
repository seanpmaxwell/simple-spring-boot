-- Jan 15, 2022

CREATE TABLE users (
    "id" SERIAL PRIMARY KEY,
    "email" VARCHAR(255) NOT NULL,
    "name" VARCHAR(255) NOT NULL,
    "pwdHash" VARCHAR(255) NULL,
    "created" TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Create case-insensitive unique-constraint
CREATE UNIQUE INDEX email_unique 
   ON main.users( LOWER(email) );

-- First dummy user item
INSERT INTO users (
    "id",
    "email",
    "name",
    "pwdHash",
    "created"
) VALUES (
    1,
    "initial.user.example.com",
    "initial user",
    "asdfasdf",
    DATE.NOW()
)
