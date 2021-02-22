# jLiteORM

A custom ORM framework that provides basic CRUD functionality. Developed in Java and influenced by Hibernate.
The framework acts as a wrapper for JDBC and facilitates persistance of user POJOs. 

## Basics 

The jLiteORM framework relies on annotations for integration with your project. POJOs must be annotated 
accordingly.  For database connectivity, the user must provide a .properties file. The framework contains three 
user exposed classes: Configuration, EntityManager, and Session. These classes must me added to your project's 
main method for the framework to function. The Session class provides the public methods for basic CRUD fuctionality. 

## Configuration(Maven)

Include the below dependency in your pom.xml:

```xml

   <dependency>
            <groupId>com.revature</groupId>
            <artifactId>jLiteORM</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

```
## Configuration(.properties file)

Must include a .properties file with the following fields:

1. url=Your URL
2. admin-usr=Your username
3. admin-ps=Your password

## Configuration(Annotations) 

Available annotations:
1. @Column - For fields that represent regular columns in the table. Annotation element "columnName" must have same name as column. 
2. @Id - For fields that represent primary keys in the table. Annotation element "columnName" must have same name as column.
3. @Table - For the class. Represents the table name. Annotation element "tableName" must have same name as the table.
4. @PrimaryKey - Another option for fields that represent primary keys in the table. Annotation element "columnName" must have same name as column.
5. @ForeignKey - For fields that represent foreign key columns in the table. Annotation element "columnName" must have same name as column.
6. @Serial - For fields that represent serial columns in the table. Annotation element "columnName" must have same name as column.
7. @JoinColumn - For fields that represent join columns in the table. Annotation element "columnName" must have same name as column.

## Configuration(User-Exposed Classes for main method) 

1. Configuration class: Add your annotated POJOs as seen below.

```Java

Configuration cfg = new Configuration();
    cfg.addAnnotatedClass(YourClass.class).addAnnotatedClass(YourClass2.class);

```
2. EntityManager class: Must add below code.

```Java

 EntityManager eM = new EntityManager(cfg.getMetamodels());

```
3. Session class: Must add below code.

```Java

 Session sesh = eM.getSession();

```
## Usage 

The following public methods are available through the Session class:

1. public void save(Object obj)
2. public List<?> findAll(Object obj)
3. public List<?> findSome(Object obj, String... theColumns)
4. public void update(Object objAfter, Object objBefore)
5. public void delete(Object obj)

## Upgrades to follow

1. Log4J
2. JUnit / Mockito












