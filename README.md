# Proto Tools

## Intro

Vaadin 21 components for creating quick prototype applications or otherwise 
rapid small application development. This is sort of mini framework on top
of Vaadin. The components are opinionated. The API of the components is kept
purposefully minimal. 

## Components

### Form<T>

Form that populates automatically based on the bean, using FieldFactory

List or Bean properties can be added too (uses PopupListEdit or PopupForm)

### ListEdit<T>

List editor based on AutoGrid

### AutoGrid<T>

Grid with Editor auto generation

List or Bean properties can be added too (uses PopupListEdit or PopupForm)

### PopupListEdit<T>

ListEdit in a Dialog

### PopupForm<T>

Form in a Dialog

### GridCrud<T>

Simple Grid + Form CRUD view.

### MenuLayout

Component extending AppLayout with menu automatically generated from Route registry.
The @PageTitle is used for menu titles and class name as fall back.

## Data model

Many of the components are using BeanValidationBinder internally. This means that
for data validation you can use JSR-303 annotations in the entities.

Components use FieldFactory which uses the following data type mapping

* String -> TextField
* Date, LocalDateTime -> DateTimePicker
* LocalDate -> DatePicker
* Enum -> ComboBox (Enum constants are automatically used as set of items to select from)
* Boolean -> Checkbox
* Double -> NumberFied
* Integer -> IntegerField
* LocalTime -> TimePicker
* BigDecimal -> BigDecimal

## Development instructions

JavaScript modules can either be published as an NPM package or be kept as local 
files in your project. The local JavaScript modules should be put in 
`src/main/resources/META-INF/frontend` so that they are automatically found and 
used in the using application.

If the modules are published then the package should be noted in the component 
using the `@NpmPackage` annotation in addition to using `@JsModule` annotation.


Starting the test/demo server:
1. Run `mvn jetty:run`.
2. Open http://localhost:8080 in the browser.

## Publishing to Vaadin Directory

You can create the zip package needed for [Vaadin Directory](https://vaadin.com/directory/) using
```
mvn versions:set -DnewVersion=1.0.0 # You cannot publish snapshot versions 
mvn install -Pdirectory
```

The package is created as `target/prototools-1.0.0.zip`

For more information or to upload the package, visit https://vaadin.com/directory/my-components?uploadNewComponent
