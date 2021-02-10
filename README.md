# Automation Studio

## Requirements (Eclipse for sources)
```
- Eclipse 2019-06 (https://www.eclipse.org/downloads/packages/release/2019-06/r)
  or
- Eclipse 2020-03 (https://www.eclipse.org/downloads/packages/release/2020-03)
- Java 1.8.0_201 (tested version)
```
### Required Eclipse plugins

Menu -> Help -> Eclipse Marketplace

```
- In filter Find: type "fx" and Install e(fx)clipse -latest version
- In filter Find: type "guava" and Install Guava Eclipse Plugin -latest version
- In filter Find: type "web" and Install Eclipse Web Developer Tools -latest version
```
Menu- > Help -> Install new software
```
work with: http://download.eclipse.org/releases/2019-06 or (http://download.eclipse.org/releases/2020-03)
Modeling
- EMF - Eclipse Modeling Framework SDK
- EMF - Model Query SDK
- EMF - Model Transaction SDK
- EMF - Validation Framework SDK
- Model comparison (EMF Compare)	3.3.10.202003070301
- MWE SDK 
- XSD - XML Schema Definition SDK
- Xpand SDK
Web, XML, Java EE and OSGi Enterprise Development
- Eclipse Faceted Project Framework
UML
- UML2 Extender SDK
- UML2 Extender SDK Developer Resource
- OCL Classic SDK: Ecore/UML Parsers,Evaluator,Edit
```
```
work with: http://download.eclipse.org/eclipse/updates/4.4
Equinox Target Components
- Equinox Target Components
```

### Import Eclipse projects

Eclipse projects are structured inside
```
- bundles
-- AutomationStudio plugin projects
- features
-- com.bichler.astudio.feature (Eclipse feature definition)
- releng
-- com.bichler.astudio.target(Eclipse target definition)
-- com.bichler.automationstudio.product (Eclipse product definition)
```
Import all projects available in those folders

### Eclipse on Linux
```
Install Eclipse Lucene package
```

### Run in Eclipse

In project com.bichler.automationstudio.product -> AS-Feature.product -> Launch an Eclipse application


## Install AutomationStudio (Windows *.exe)

### Download AutomationStudio

Download latest version of AutomationStudio from https://www.bichler.tech/ and follow installer instructions


## Install C-Compile Toolchain

### Download toolchain.zip

Download toolchain.zip file from https://www.bichler.tech/

### Install toolchain in AutomationStudio

#### Start AutomationStudio

Menu -> Help -> Install toolchain

Select path of the toolchain.zip file