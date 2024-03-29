# Progetto Ingegneria del Software 2021 - Politecnico di Milano

![logo](src/main/resources/logo.png)

## Software Engineering group project, academic year 2020 - 2021, Politecnico di Milano

Implementation of the Maestri del Rinascimento board game on PC, entirely written in Java, designed to be an online multi-player game (up to 4 players)

__Members__ of the development group:
* Alessandro Gavardi - [@AlessandroGavardi](https://github.com/AlessandroGavardi)
* Simone Giampà - [@SimonGiampy](https://github.com/SimonGiampy)
* Michele Guerrini - [@micheleguerrini](https://github.com/micheleguerrini)

#### Repository code: ing-sw-2021-gavardi-giampa-guerrini

#### UML Class Diagrams
We created a set of different UML diagrams. The complete diagrams show every class in the repository, both with
and without their methods, variables and constructors. The simplified diagram shows a schema of the main components
and classes of the code repository.
- the diagrams are uploaded [here](https://github.com/SimonGiampy/ing-sw-2021-gavardi-giampa-guerrini/tree/main/deliverables/UML)

#### Sequence Diagrams 

- are uploaded [here](https://github.com/SimonGiampy/ing-sw-2021-gavardi-giampa-guerrini/tree/main/deliverables/Sequence%20Diagrams)

#### Jar Executables

- are uploaded [here](https://github.com/SimonGiampy/ing-sw-2021-gavardi-giampa-guerrini/tree/main/deliverables/JARs)

-------

### Libraries used

Library | Description
--------|------------
Maven | Automation instrument used for compilation and project dependencies management
JavaFX | Graphics library used for the implementation of the visual user interface
JUnit 5 | Unit testing framework (latest version)

### Implemented functionalities

* Complete rules (single player and multi player game modes)
* CLI (command line interface)
* GUI (graphic user interface)
* Socket connection for the multi player online game
* Advanced functionalities implemented
    * **Offline Single-Player Match**: The single player game mode can be played offline, without the need of connecting to a server for playing.
        The client application contains all the necessary classes to run the complete game. But in the online game mode, only the 
        server application has full access to the game logic model.
    * **Multiple Matches**: The server handles multiple matches at the same time. Each match is managed by a lobby, that can
        contain up to 4 players. The access to a lobby is arbitrary: a player can choose to create its own lobby, or
        join an existing one, based on the number of players present in the lobby. The nicknames chosen by the players
        must be unique in every lobby (but they don't necessarily need to be unique across different lobbies).

### Details about the implementation, and our additions to the game

* __GUI resolution__ is set to 1920 x 1080, and the game runs on full-screen mode. Note that the scale layout for the screen must be
        set to 100%, otherwise the game might not be displayed in the correct window size.
  
* __Model implementation for customizable configuration__: the _Game Logic Model_ works perfectly with custom game configurations.
        Our initial goal was to implement the game configurator, but we ended up not doing it because of the lack of available time.
        So the game configuration XML file can be edited manually, and consequently the _Model_ logic adapts to it.
        The _CLI application_ can adapt to every possible configuration, while showing every card and faith track correctly.
        The _GUI application_ is made only for the standard configuration file, which means that modifying the parameters
        will break it.
  
* __Disconnection handling when accessing a lobby__: when a client disconnects from the server during the lobby access phase,
        the lobby is not lost. If the player who disconnected was the lobby host, then the server assigns the host role
        another client connected to the same lobby (if there are any). Otherwise, if the host who disconnected was the only 
        one present in the lobby, then the server deletes the lobby. If a client disconnects from the lobby before or after 
        choosing their nickname, then the server frees that spot for other clients, so that anyone can connect to the lobby 
        (if in the meantime it didn't fill up).

-------

### Compilation

We uploaded the pre-compiled jar executable files. The uploaded jars were created with the aid of the Maven Assembly Plugin.
These jar packages are proven to work correctly on Windows machines. If any problem arises when executing the jars on a different
operating system, regenerating the jar packages might be needed.

In order to compile the jar file autonomously, change the current folder to the project root directory and write:
```shell
 mvn clean package
```
This will create all 3 jar file in the `builds` folder.

-------

### Execution

There are 3 executable jar packages in this repository that you can download it [here](https://github.com/SimonGiampy/ing-sw-2021-gavardi-giampa-guerrini/tree/main/deliverables/JARs).
* __Server app__: executable for running the server on the local machine (localhost).
* __GUI app__: graphical user interface (needs JavaFX to be installed).
* __CLI app__: command line interface (emojis and special characters are shown only when executing it via the IntelliJ Idea run terminal).

Before executing the programs, make sure the terminal is set in the correct directory. Write this code in command line
in order to execute an application.
```shell
 java -jar PSP17-server_app.jar
 java -jar PSP17-gui_app.jar
 java -jar PSP17-cli_app.jar
```

An __Important Note__ about the game execution on the Terminal (game lauched with CLI mode):

The game can be played correctly only on the IntelliJ Idea Terminal. Running the game on other consoles may not show correctly all the symbols and colors used in the game. We use several symbols and emojis for displaying the market, resources, and all the cards in the game. After many tests with different consoles and various software, we weren't able to play the game with all the symbols and colors shown as expected on a console different from the IntelliJ Idea one. 

-------

#### Troubleshooting

In case this error shows up when executing the GUI jar executable
> Error: JavaFX runtime components are missing, and are required to run this application

add this parameter in the command line when executing the jar:
```shell
 java --module-path path\to\JavaFX\lib --add-modules javafx.controls,javafx.fxml -jar PSP17-gui_app.jar
```
where `path\to\JavaFX\lib` stands for the path where the javafx library is installed on the local machine.
The game is set up so that this error should not show up. 
Furthermore, it is required to have the `java` command listed in the system environment variables, in order for the game to be run on the machine from all directories.
