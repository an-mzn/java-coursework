# Java coursework

Four programming assignments completed during my first year studying Computer Science at King's College London. These are learning projects, preserved with their original assets and reports. They use course starter material and, where noted, collaborative work.

## Projects

- [Image Viewer](image-viewer/) (basic) — a desktop image album with navigation, image effects and statistics. Demonstrates collections, object-oriented design and Swing interfaces.
- [The Lost Sword of Lunareth](lost-sword-of-lunareth/) — a text adventure with rooms, characters, combat and inventory. Demonstrates classes, inheritance, collections and command parsing. Earlier source variants are available in the repository's initial commit.
- [Wildlife Simulation](wildlife-simulation/) — a grid simulation of animals, plants and weather. Demonstrates inheritance, simulation rules, streams and state updates. The source has a subsequent fix for offspring creation; the included historical JAR predates that fix.
- [London Air Pollution Viewer](london-air-pollution/) — a JavaFX application for exploring air pollution data. Demonstrates data loading, maps, filtering, statistics and charts.

## Running

Use JDK 17 or later. From each project directory:

- Image Viewer: open the project in BlueJ and create an `ImageViewer` instance, or run `java -jar AyanMamunCW1.jar`.
- The Lost Sword of Lunareth: run `javac *.java` followed by `java Main`, or run `java -jar TheLostSwordOfLunareth.jar`.
- Wildlife Simulation: run `javac $(find simulation -name '*.java')` followed by `java simulation.Main`. An optional first argument sets the number of steps. The original `PPA3_FINAL.jar` is also included.
- London Air Pollution Viewer: open the BlueJ project with JavaFX available, then run `MapViewer` as a JavaFX application. The project requires JavaFX and the included `UKAirPollutionData` directory; JavaFX is not included in the JDK.

The JAR files are historical submission artifacts and may differ from the current source. Graphical projects need a desktop environment.

## Tests

The focused JUnit 5 tests cover animal births and game inventory. With JUnit Platform Console Standalone 1.11.4 downloaded as `junit.jar`, from the repository root:

```sh
mkdir -p /tmp/java-coursework-tests
javac -cp junit.jar -d /tmp/java-coursework-tests lost-sword-of-lunareth/{Item,Inventory}.java $(find wildlife-simulation/simulation tests -name '*.java')
java -jar junit.jar execute --class-path /tmp/java-coursework-tests --scan-class-path
```

The air pollution project also retains `PollutionStatsTabTest.java`, credited to Abeer Hussain. It requires JUnit and JavaFX and is not included in the command above.

## Authorship and provenance

The image viewer and other assignments build on course starter code by Michael Kölling and David J. Barnes, as credited in the source. The image viewer and adventure source identify Ayan Mamun alongside starter-code authors. The wildlife simulation credits Karim Malik and Ayan Mamun alongside the starter-code authors. The air pollution viewer was collaborative; its source credits Ayan Mamun, Raihan Islam, Abeer Hussain and Karim He, with individual credits on classes. In particular, `YearComparisonTab` and `TabStyler` credit Ayan Mamun. See each file for its own attribution. These credits do not establish every person's exact contribution; the repository began as a bulk upload rather than preserving the original development history.
