# ProtoTools

## Intro

Vaadin 21 components for creating quick prototype applications or otherwise 
rapid small application development. This is sort of mini framework on top
of Vaadin. The components are opinionated. The API of the components is kept
purposefully minimal. Thus ProtoTools can be seen as a opionated mini
framework for low code application development.

## Components

### Form<T>

Form that populates automatically based on the bean, using FieldFactory
(see DataModel)

List or Bean properties can be added too (uses PopupListEdit or PopupForm)

![Form](https://github.com/TatuLund/ProtoTools/blob/master/Form.jpg?raw=true)

### ListEdit<T>

List editor based on AutoGrid. So it inherits its responsive features. Value
of the ListEdit is the list of the items in it. I.e. this is not a selection
component.

![Form](https://github.com/TatuLund/ProtoTools/blob/master/ListEdit.jpg?raw=true)

### AutoGrid<T>

Grid with Editor auto generation using FieldFactory (see DataModel)

List or Bean properties can be added too (uses PopupListEdit or PopupForm)

![AutoGrid](https://github.com/TatuLund/ProtoTools/blob/master/AutoGridWide.jpg?raw=true)

Grid is responsive, when you resize browser to be narrow, only one compact column with
four first properties is shown and editor is shown as popup dialog instead.

![AutoGrid responsive](https://github.com/TatuLund/ProtoTools/blob/master/AutoGridResponsive.jpg?raw=true)

### PopupListEdit<T>

ListEdit in a Dialog

### PopupForm<T>

Form in a Dialog

### GridCrud<T>

Simple Grid + Form aside Grid CRUD view.

![GridCrud](https://github.com/TatuLund/ProtoTools/blob/master/GridCrud.jpg?raw=true)

### PopupCrud<T>

Simple Grid + Form as popup dialog CRUD view.

![PopupCrud](https://github.com/TatuLund/ProtoTools/blob/master/PopupCrud.jpg?raw=true)

### Dashboard

Rudimentary Dashboard component with drag and drop support. New widgets can be added and widgets can be replaced (e.g. for content / data update).

### Wizard
Wizard aka Stepper type component for splitting complex forms to multiple paged sub-forms.

### MenuLayout

Component extending AppLayout with menu automatically generated from Route registry.
The @PageTitle is used for menu titles and class name as fall back. Routes without
parameters are generated as RouteLinks, and Routes with parameters as TextFields
where you can input the parameter. (Route templates are not supported)

![Menu responsive](https://github.com/TatuLund/ProtoTools/blob/master/MenuResponsive.jpg?raw=true)

## Data model

ProtoTools is purposed for simple applications, which you want to build fast.
Thus data model is assumed to be relatively straightforward.

Many of the components are using BeanValidationBinder internally. This means that
for data validation you can use JSR-303 annotations in the entities.

Components use FieldFactory which uses the following data type mapping of the
bean properties.

* String -> TextField
* Date, LocalDateTime -> DateTimePicker
* LocalDate -> DatePicker
* Enum -> ComboBox (Enum constants are automatically used as set of items to select from)
* Boolean -> Checkbox
* Double -> NumberFied
* Integer -> IntegerField
* LocalTime -> TimePicker
* BigDecimal -> BigDecimal

Other data types are just skipped and custom fields are not supported.

ProtoTools could be used also when data model is slightly more complex by introducing
DTO layer that fits in.

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
