# PCM2Java4Key
Generate Java code to verify Palladio models using KeY

Generate Java code from architecture models that were created using the [Palladio Component Model (PCM)](http://palladio-simulator.com/) with proof obligations for verifying the confidentiality of data flows in the Java code using [KeY](http://www.key-project.org) after manual completion. 

See also [PCM2Prolog](https://github.com/KASTEL-SCBS/PCM2Prolog) for analysing the confidentiality of data flows in architecture models using a superset of the confidentiality specification for verification.

## Installation
* Even if you already have an Eclipse you should always download a new Eclipse for PCM2Java4KeY (in order not to mix up versions).
* Download and run a clean [**Oxygen** Release of the **Eclipse** IDE for Java and **DSL Developers*](https://www.eclipse.org/downloads/packages/eclipse-ide-java-and-dsl-developers/oxygenr). Do not use another Eclipse Package, i.e. also not the one for Java developers.
* Install the PCM2Java4KeY plug-ins from the [nightly update site](https://sdqbuild.ipd.kit.edu/nexus/content/repositories/kastel-scbs)
  * Help - Install New Software... - Add... (paste the URL from above, it only works in Eclipse)

## Usage
### Run Eclipse 
* Run the Eclipse with the PCM2Java4KeY plug-ins installed in the previous step
* Create or modify a new or existing Palladio model in the workspace of the new Eclipse
  * e.g. import an existing project ("Import - Existing Projects into Workspace")
  * e.g. clone and import an example from the [Examples Repository](https://github.com/KASTEL-SCBS/Examples4SCBS)
  * e.g. right-click - New - Other - Palladio Modeling - New Palladio Project

### Create Architecture Model and Confidentiality Specification in Eclipse
* To model the component repository, system assembly, resource environment, and allocation you can use the wizard and graphical or tree-based editors of Palladio
  * e.g. using "New - Palladio Modeling - PCM ... Diagram"
* To create a confidentiality specification model you can use the wizard and tree-based editor
  * New - Other - Example EMF Model Creation Wizards - Confidentiality Model (keep "Specification" as "Model Object")
* To use the confidentiality specification in Palladio models you have to use the tree-based editor
  * open the tree-based editor by opening, for example, a .repository file with a double-click or right-click "Open with - Repository Model Editor"
  * right-click on the root element of a Palladio model (i.e. a "Repository", "System", "Resource Environment", or "Allocation" element)
  * Select "MDSD Profiles - Apply/Unapply Profiles" and add "Profile PCMConfidentialityProfile" in the dialog
  * Select the model element to which a confidentiality specification shall be added, right-click "MDSD Profiles - Apply/Unapply Stereotypes" and add the appropriate stereotype
  
### Generate Java Code to be completed and verified
* Select a repository model
* Right-click "KASTEL Code Analysis - Create Java Code for KeY"

## Development
If you want to extend or modify the code of PCM2Java4KeY, follow the instructions on the [Developing PCM2Java4KeY wiki page](https://github.com/KASTEL-SCBS/PCM2Java4Key/wiki/Developing-PCM2Java4KeY).
