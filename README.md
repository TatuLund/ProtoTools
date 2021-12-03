# Proto Tools

Vaadin 21 components for creating quick prototype applications

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
