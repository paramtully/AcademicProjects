DROP TABLE REPRESENTS;
DROP TABLE PLAY;
DROP TABLE HAS;
DROP TABLE COMPANYSPONSORSPLAYER;
DROP TABLE COACHMANAGES;
DROP TABLE GMMANAGES;
DROP TABLE COMMISSIONERMANAGESLEAGUE;
DROP TABLE COMMISSIONERMANAGESTEAM;
DROP TABLE PLAYERSPLAYFORTEAM;
DROP TABLE COMPANYSPONSORSTEAM;
DROP TABLE TEAMSCONTAINEDINLEAGUE;
DROP TABLE LEAGUES;
DROP TABLE AGENTS;
DROP TABLE AFFILIATEDCOMPANIES;
DROP TABLE GAMESINARENAS;
DROP TABLE ARENAS;

CREATE TABLE Leagues (
    name VARCHAR(50) PRIMARY KEY,
    hq VARCHAR(80)
);

CREATE TABLE TeamsContainedInLeague (
    name VARCHAR(50),
    city VARCHAR(50),
    leagueName VARCHAR(50) NOT NULL,
    championships int NOT NULL,
    owner VARCHAR(50) NOT NULL,
    wins int,
    losses int,
    PRIMARY KEY(name, city),
    FOREIGN KEY(leagueName) REFERENCES Leagues(name) ON DELETE CASCADE
);

CREATE TABLE PlayersPlayForTeam (
    name VARCHAR(100),
    jerseyNum int,
    position VARCHAR(30),
    teamName VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    height int,
    weightKG int,
    ppg float(5),
    rpg float(5),
    apg float(5),
    awards int,
    years int,
    annualSalary int,
    PRIMARY KEY(name, jerseyNum, position),
    FOREIGN KEY (teamName, city) REFERENCES TeamsContainedInLeague(NAME, CITY) ON DELETE CASCADE,
    CHECK ( jerseyNum >= 0 AND jerseyNum < 100 AND annualSalary >= 0)
);

CREATE TABLE CoachManages (
    name VARCHAR(50) PRIMARY KEY ,
    city VARCHAR(50),
    teamName VARCHAR(50),
    yearsExperience int NOT NULL,
    coachOfYears int NOT NULL,
    FOREIGN KEY(teamName, city) REFERENCES TeamsContainedInLeague(NAME, CITY)
    ON DELETE CASCADE
);

CREATE TABLE GMManages (
    name VARCHAR(50) PRIMARY KEY ,
    city VARCHAR(50),
    teamName VARCHAR(50),
    yearsExperience int NOT NULL,
    gmOfYears int NOT NULL,
    FOREIGN KEY(teamName, city) REFERENCES TeamsContainedInLeague(NAME, CITY)
    ON DELETE CASCADE
);

CREATE TABLE CommissionerManagesTeam (
    name VARCHAR(50) PRIMARY KEY,
    teamName VARCHAR(50),
    city VARCHAR(50),
    yearsExperience int NOT NULL,
    FOREIGN KEY(teamName, city) REFERENCES TeamsContainedInLeague(NAME, CITY) ON DELETE CASCADE
);

CREATE TABLE Agents (
    agency VARCHAR(70),
    name VARCHAR(50),
    PRIMARY KEY(agency, name)
);

CREATE TABLE AffiliatedCompanies (
    name VARCHAR(80) PRIMARY KEY,
    productType VARCHAR(80),
    valuation int
);

CREATE TABLE CompanySponsorsTeam (
    companyName VARCHAR(80),
    teamName VARCHAR(50),
    city VARCHAR(50),
    amount int,
    PRIMARY KEY(companyName, teamName, city),
    FOREIGN KEY(companyName) REFERENCES AffiliatedCompanies(name) ON DELETE CASCADE,
    FOREIGN KEY(teamName, City) REFERENCES TeamsContainedInLeague(NAME, CITY) ON DELETE CASCADE
);

CREATE TABLE CompanySponsorsPlayer (
    companyName VARCHAR(80),
    playerName VARCHAR(100),
    jerseyNum int,
    position VARCHAR(30),
    amount int,
    PRIMARY KEY(companyName, playerName, jerseyNum, position),
    FOREIGN KEY(companyName) REFERENCES AffiliatedCompanies(name) ON DELETE CASCADE,
    FOREIGN KEY(playerName, jerseyNum, position) REFERENCES PlayersPlayForTeam(name, jerseyNum, position) ON DELETE CASCADE
);

CREATE TABLE Arenas (
    name VARCHAR(50),
    city VARCHAR(50),
    numSeats int,
    seatPrice int,
    maxRevenue int,
    PRIMARY KEY(name, city)
);

CREATE TABLE GamesInArenas (
    gameDate date NOT NULL,
    homeWon int,
    arenaName VARCHAR(50),
    city VARCHAR(50),
    numSeatsSold int,
    PRIMARY KEY(arenaName, city, gameDate),
    FOREIGN KEY(arenaName, city) REFERENCES Arenas(name, city) ON DELETE CASCADE,
    CHECK ( homeWon = 0 OR homeWon = 1 )
);


CREATE TABLE Play (
    arenaName VARCHAR(50),
    arenaCity VARCHAR(50),
    gameDate date,
    teamName VARCHAR(50),
    teamCity VARCHAR(50),
    PRIMARY KEY(arenaName, arenaCity, gameDate, teamName, teamCity),
    FOREIGN KEY(arenaName, arenaCity, gameDate) REFERENCES GamesInArenas(arenaName, city, gameDate) ON DELETE CASCADE,
    FOREIGN KEY(teamName, teamCity) REFERENCES TeamsContainedInLeague(NAME, CITY) ON DELETE CASCADE
);
--
-- -- NOTE: adding 2 way total participation constraint -> each team has 1 arena and vise versa
CREATE TABLE Has (
    arenaName VARCHAR(50),
    arenaCity VARCHAR(50),
    teamName VARCHAR(50),
    teamCity VARCHAR(50),
    PRIMARY KEY(teamName, teamCity),
    UNIQUE(arenaName, arenaCity),
    FOREIGN KEY(teamName, teamCity) REFERENCES TeamsContainedInLeague(NAME, CITY) ON DELETE CASCADE,
    FOREIGN KEY(arenaName, arenaCity) REFERENCES Arenas(name, city) ON DELETE CASCADE
);

-- -- NOTE: Assertions dont seem to be supported by oracle :)
-- -- CREATE ASSERTION arenaParticipationInHas
-- -- CHECK (NOT EXISTS ( (SELECT name, city
-- --                     FROM TeamsContainedInLeague)
-- --                     EXCEPT
-- --                     (SELECT teamname AS name, teamCity AS city
-- --                     FROM Has)));
--
-- -- CREATE ASSERTION teamParticipationInHas
-- -- CHECK (NOT EXISTS ((SELECT name, city
-- --                     FROM Arenas)
-- --                     EXCEPT
-- --                     (SELECT arenaName AS name, arenaCity AS city
-- --                     FROM Has)));
--
CREATE TABLE Represents (
    agency VARCHAR(70),
    agentName VARCHAR(50),
    playerName VARCHAR(100),
    jerseyNum int,
    position VARCHAR(30),
    PRIMARY KEY(playerName, jerseyNum, position),
    FOREIGN KEY(playerName, jerseyNum, position) REFERENCES PlayersPlayForTeam(name, jerseyNum, position) ON DELETE CASCADE,
    FOREIGN KEY(agency, agentName) REFERENCES Agents(agency, name) ON DELETE CASCADE,
    CHECK ( jerseyNum >= 0 AND jerseyNum < 100 )
);

CREATE TABLE CommissionerManagesLeague (
    commissionerName VARCHAR(50) PRIMARY KEY,
    leagueName VARCHAR(50),
    FOREIGN KEY(leagueName) REFERENCES Leagues(name) ON DELETE CASCADE,
    FOREIGN KEY(commissionerName) REFERENCES CommissionerManagesTeam(name) ON DELETE CASCADE
);

INSERT INTO Leagues VALUES ('NBA', 'New York');
INSERT INTO Leagues VALUES ('Euroleague', 'Barcelona');
INSERT INTO Leagues VALUES ('CBA', 'Beijing');
INSERT INTO Leagues VALUES ('NBL', 'Melbourne');
INSERT INTO Leagues VALUES ('NBA G League', 'New York');

INSERT INTO TeamsContainedInLeague VALUES ('Raptors', 'Toronto', 'NBA', 1, 'MLSE', 31, 32);
INSERT INTO TeamsContainedInLeague VALUES ('Lakers', 'Los Angeles', 'NBA', 17, 'Jeanie Buss', 29, 33);
INSERT INTO TeamsContainedInLeague VALUES ('Clippers', 'Los Angeles', 'NBA', 0, 'Steve Ballmer', 26, 38);
INSERT INTO TeamsContainedInLeague VALUES ('Heat', 'Miami', 'NBA', 3, 'MICKY ARISON', 34, 29);
INSERT INTO TeamsContainedInLeague VALUES ('Bulls', 'Chicago', 'NBA', 6, 'Jerry Reinsdorf', 28, 34);
INSERT INTO TeamsContainedInLeague VALUES ('Tigers', 'Guangdong', 'CBA', 11, 'Guangdong Hongyuan Group', 50, 33);
INSERT INTO TeamsContainedInLeague VALUES ('United', 'Melbourne', 'NBL', 3, 'Andrew Bogut', 30, 33);
INSERT INTO TeamsContainedInLeague VALUES ('Hornets', 'Charlotte', 'NBA', 3, 'Andrew Bogut', 10, 42);
INSERT INTO TeamsContainedInLeague VALUES ('Warriors', 'Golden State', 'NBA', 3, 'Manveer Trehan', 51, 0);

INSERT INTO AffiliatedCompanies VALUES ('Nike', 'Sports Apparel', 183000000000);
INSERT INTO AffiliatedCompanies VALUES ('Beats', 'Headphones', 20000000000);
INSERT INTO AffiliatedCompanies VALUES ('Sun Life', 'Juice', 20000000000);
INSERT INTO AffiliatedCompanies VALUES ('JBL', 'Headphones', 800000000);
INSERT INTO AffiliatedCompanies VALUES ('Gatorade', 'Sports Hydration', 19000000000);
INSERT INTO AffiliatedCompanies VALUES ('Wish', 'Knockoffs', 450000000);
INSERT INTO AffiliatedCompanies VALUES ('OVO', 'Street Apparel', 999999999999);
INSERT INTO AffiliatedCompanies VALUES ('Jordan', 'Street Apparel', 99999999999900);

INSERT INTO CommissionerManagesTeam VALUES ('Adam Silver', 'Raptors', 'Toronto', 1);
INSERT INTO CommissionerManagesTeam VALUES ('Yao Ming', 'Raptors', 'Toronto', 2);
INSERT INTO CommissionerManagesTeam VALUES ('Milos Teodosic', 'Raptors', 'Toronto', 3);
INSERT INTO CommissionerManagesTeam VALUES ('David Stern', 'Raptors', 'Toronto', 4);
INSERT INTO CommissionerManagesTeam VALUES ('Alex Jones', 'Raptors', 'Toronto', 5);

INSERT INTO CoachManages VALUES ('Nick Nurse', 'Toronto', 'Raptors', 18, 1);
INSERT INTO CoachManages VALUES ('Erik Spoelstra', 'Miami', 'Heat', 25, 1);
INSERT INTO CoachManages VALUES ('John Smith', 'Guangdong', 'Tigers', 8, 2);
INSERT INTO CoachManages VALUES ('God Shammgod', 'Melbourne', 'United', 26, 3);
INSERT INTO CoachManages VALUES ('Darvin Ham', 'Los Angeles', 'Lakers', 1, 0);

INSERT INTO GMManages VALUES ('Masai Ujiri', 'Toronto', 'Raptors', 15, 1);
INSERT INTO GMManages VALUES ('RC Buford', 'Miami', 'Heat', 29, 1);
INSERT INTO GMManages VALUES ('John Scott', 'Guangdong', 'Tigers', 4, 0);
INSERT INTO GMManages VALUES ('Matthew Dellavadova', 'Melbourne', 'United', 2, 0);
INSERT INTO GMManages VALUES ('Sam Presti', 'Los Angeles', 'Clippers', 10, 1);

INSERT INTO CommissionerManagesLeague VALUES ('Adam Silver', 'NBA');
INSERT INTO CommissionerManagesLeague VALUES ('Yao Ming', 'CBA');
INSERT INTO CommissionerManagesLeague VALUES ('Milos Teodosic', 'Euroleague');
INSERT INTO CommissionerManagesLeague VALUES ('David Stern', 'NBA');
INSERT INTO CommissionerManagesLeague VALUES ('Alex Jones', 'NBL');

INSERT INTO CompanySponsorsTeam VALUES ('Nike', 'Lakers', 'Los Angeles', 50000000);
INSERT INTO CompanySponsorsTeam VALUES ('OVO', 'Raptors', 'Toronto', 20000000);
INSERT INTO CompanySponsorsTeam VALUES ('Wish', 'Lakers', 'Los Angeles', 15000000);
INSERT INTO CompanySponsorsTeam VALUES ('Sun Life', 'Raptors', 'Toronto', 15000000);
INSERT INTO CompanySponsorsTeam VALUES ('Jordan', 'Hornets', 'Charlotte', 0);

INSERT INTO PlayersPlayForTeam VALUES ('Kawhi Leonard', 2, 'SF', 'Clippers', 'Los Angeles', 201, 105, 30.1, 10.2, 5.4, 10, 3, 44500000);
INSERT INTO PlayersPlayForTeam VALUES ('LeBron James', 6, 'PF', 'Lakers', 'Los Angeles', 211, 115, 30.6, 8.2, 8.4, 20, 2, 50540000);
INSERT INTO PlayersPlayForTeam VALUES ('Joe Ingles', 7, 'PG', 'United', 'Melbourne', 220, 108, 22.3, 6.4, 2.2, 4, 5, 12500000);
INSERT INTO PlayersPlayForTeam VALUES ('Ben Simmons', 10, 'PG', 'Tigers', 'Guangdong', 232, 125, 8.3, 10.6, 11.4, 2, 4, 7800000);
INSERT INTO PlayersPlayForTeam VALUES ('DeMar DeRozan', 11, 'SG', 'Bulls', 'Chicago', 197, 100, 26.6, 5.2, 6.7, 8, 3, 32500000);

INSERT INTO CompanySponsorsPlayer VALUES ('Nike', 'LeBron James', 6, 'PF', 50000000);
INSERT INTO CompanySponsorsPlayer VALUES ('Wish', 'Kawhi Leonard', 2, 'SF', 15000000);
INSERT INTO CompanySponsorsPlayer VALUES ('Beats', 'LeBron James', 6, 'PF', 10000000);
INSERT INTO CompanySponsorsPlayer VALUES ('Gatorade', 'Joe Ingles', 7, 'PG', 12000000);
INSERT INTO CompanySponsorsPlayer VALUES ('JBL', 'LeBron James', 6, 'PF', 8000000);

INSERT INTO Arenas VALUES ('American Airlines Arena', 'Miami', 19700, 80, 1576000);
INSERT INTO Arenas VALUES ('Crypto.com Arena', 'Los Angeles', 20000, 120, 2400000);
INSERT INTO Arenas VALUES ('Scotiabank Arena', 'Toronto', 19500, 130, 2535000);
INSERT INTO Arenas VALUES ('Staples Center', 'Los Angeles', 20000, 120, 2400000);
INSERT INTO Arenas VALUES ('Oracle Arena', 'Oakland', 19100, 150, 2865000);

INSERT INTO GamesInArenas VALUES (Date'2022-02-21', 1, 'American Airlines Arena', 'Miami' , 19000);
INSERT INTO GamesInArenas VALUES (Date'2020-01-21', 1, 'Crypto.com Arena', 'Los Angeles', 20000);
INSERT INTO GamesInArenas VALUES (Date'2021-11-18', 0, 'Scotiabank Arena', 'Toronto', 19500);
INSERT INTO GamesInArenas VALUES (Date'2021-11-16', 1, 'Scotiabank Arena', 'Toronto', 19500);
INSERT INTO GamesInArenas VALUES (Date'2020-03-10', 0, 'Staples Center', 'Los Angeles', 20000);
INSERT INTO GamesInArenas VALUES (Date'2022-03-10', 0, 'Oracle Arena', 'Oakland', 20000);

INSERT INTO Play VALUES ('American Airlines Arena', 'Miami', Date'2022-02-21', 'Heat', 'Miami');
INSERT INTO Play VALUES ('American Airlines Arena', 'Miami', Date'2022-02-21', 'Lakers', 'Los Angeles');
INSERT INTO Play VALUES ('Crypto.com Arena', 'Los Angeles', Date'2020-01-21', 'Clippers', 'Los Angeles');
INSERT INTO Play VALUES ('Crypto.com Arena', 'Los Angeles', Date'2020-01-21', 'Raptors', 'Toronto');
INSERT INTO Play VALUES('Scotiabank Arena', 'Toronto', Date'2021-11-18', 'Raptors', 'Toronto');
INSERT INTO Play VALUES ('Scotiabank Arena', 'Toronto', Date'2021-11-18', 'Heat', 'Miami');
INSERT INTO Play VALUES ('Scotiabank Arena', 'Toronto', Date'2021-11-16', 'Raptors', 'Toronto');
INSERT INTO Play VALUES ('Scotiabank Arena', 'Toronto', Date'2021-11-16', 'Lakers', 'Los Angeles');
INSERT INTO Play VALUES ('Staples Center', 'Los Angeles', Date'2020-03-10', 'Lakers', 'Los Angeles');
INSERT INTO Play VALUES ('Staples Center', 'Los Angeles', Date'2020-03-10', 'Clippers', 'Los Angeles');
INSERT INTO Play VALUES ('Oracle Arena', 'Oakland', Date'2022-03-10', 'Warriors', 'Golden State');
INSERT INTO Play VALUES ('Oracle Arena', 'Oakland', Date'2022-03-10', 'Lakers', 'Los Angeles');

INSERT INTO Has VALUES ('American Airlines Arena', 'Miami', 'Heat', 'Miami');
INSERT INTO Has VALUES ('Crypto.com Arena', 'Los Angeles', 'Clippers', 'Los Angeles');
INSERT INTO Has VALUES ('Scotiabank Arena', 'Toronto', 'Raptors', 'Toronto');
INSERT INTO Has VALUES ('Staples Center', 'Los Angeles', 'Lakers', 'Los Angeles');
INSERT INTO Has VALUES ('Oracle Arena', 'Oakland', 'Warriors', 'Golden State');

INSERT INTO Agents VALUES ('Klutch Sports', 'Rich Paul');
INSERT INTO Agents VALUES ('Gold Star', 'Dan Milstein');
INSERT INTO Agents VALUES ('A1 Agents', 'Marc Crawford');
INSERT INTO Agents VALUES ('ThirtyFive Ventures', 'Rich Kleiman');
INSERT INTO Agents VALUES ('BBA', 'Randy Coleman');

INSERT INTO Represents VALUES ('Klutch Sports', 'Rich Paul', 'LeBron James', 6, 'PF');
INSERT INTO Represents VALUES ('Gold Star', 'Dan Milstein', 'Kawhi Leonard', 2, 'SF');
INSERT INTO Represents VALUES ('A1 Agents', 'Marc Crawford', 'Joe Ingles', 7, 'PG');
INSERT INTO Represents VALUES ('ThirtyFive Ventures', 'Rich Kleiman', 'Ben Simmons', 10, 'PG');
INSERT INTO Represents VALUES ('BBA', 'Randy Coleman', 'DeMar DeRozan', 11, 'SG');


