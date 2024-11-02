# Concurrent Data Acquisition System

This project addresses a concurrent data acquisition system that includes two buffers and three types of actors: Data Creators, Data Reviewers, and Data Consumers. The operational cycle involves data creation, review, and consumption.

### Authors:
- **Franco Nicolas Bottini**
- **Valentin Robledo**
- **Aquiles Benjamín Lencina**
- **Julieta Bernaus**
- **Augusto Gabriel Cabrera**

## Code Structure

### Main Classes:

- **Data**: Represents the data structure used in the program.
- **Buffer**: An abstract class modeling a data storage space.
- **InitBuffer**: Extends Buffer and represents the initial data storage.
- **ValidBuffer**: Extends Buffer and stores validated data.
- **Creator**: Implements the Runnable interface and is responsible for data creation.
- **Reviewer**: Implements the Runnable interface and reviews stored data.
- **Consumer**: Implements the Runnable interface and consumes validated data.
- **Log**: Extends Thread and records execution statistics.

### Code Operation

1. **Initialization**: Instances of the classes are created, and the threads corresponding to the actors (creators, reviewers, consumers) and log are started.

2. **Data Creation**: The creators generate data and deposit it in the Initial Buffer (`InitBuffer`).

3. **Data Review**: The reviewers verify the data in the Initial Buffer. When all reviewers have reviewed a piece of data, the last reviewer copies it to the Validated Buffer (`ValidBuffer`).

4. **Data Consumption**: The consumers retrieve data from the Validated Buffer and remove the corresponding copy in the Initial Buffer.

5. **Logging and Statistics**: The log records detailed statistics, including execution time, the number of data items in each buffer, and each actor's actions.

## Class Diagram

The program structure is represented in the following class diagram:

<p align="center">
  <img src="img/Diagrama%20de%20Clases.jpg" alt="Class Diagram">
</p>

## Sequence Diagram

The process of creating, reviewing, and consuming a data item is illustrated in the following sequence diagram:

<p align="center">
  <img src="img/Diagrama%20de%20Secuencia.jpg" alt="Sequence Diagram">
</p>
