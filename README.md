# PCM2Java4Key
Generate Java code to verify Palladio models using KeY

Generate Java code from architecture models that were created using the Palladio Component Model (PCM) with proof obligations for verifying the confidentiality of data flows in the Java code after manual completion. 

See also [PCM2Prolog](https://github.com/KASTEL-SCBS/PCM2) for analysing the confidentiality of data flows in architecture models using a superset of the confidentiality specification for verification.

## Development
### Prepare Development Eclipse
* Download and run a clean [**Neon2** Release of the **Eclipse** IDE for Java and **DSL Developers*](https://www.eclipse.org/downloads/packages/eclipse-ide-java-and-dsl-developers/neon2). Do not use another Eclipse Package, i.e. also not the one for Java developers.
* Install Eclipse **OCL 6.0.1** using the Eclipse Marketplace
  * Help - Eclipse Marketplace ... - Search for "OCL"
* Install **Palladio 4.0** from the [Palladio Simulator nightly builds site](https://sdqweb.ipd.kit.edu/eclipse/palladiosimulator/nightly/)
  * Help - Install New Software...- Add...
  * Select at least **all "Palladio Bench Core Features"** and the **"MDSD Profiles"** feature of the "Palladio Supporting Features" category 

### Clone Repository and Import Projects
* Temporary workaround: Clone the [Vitruv repository](https://github.com/vitruv-tools/Vitruv) and import only the ["tools.vitruv.framework.util" project](https://github.com/vitruv-tools/Vitruv/tree/master/bundles/framework/tools.vitruv.framework.util) into your workspace
* Clone the [PCM2Java4KeY repository](https://github.com/KASTEL-SCBS/PCM2Java4Key) **and its submodules** and import all Eclipse plug-in projects (aka bundles) in it into your workspace
  * both can be done at once in Eclipse
    * right-click in the Package Explorer - Import - Git - Projects from Git - Clone URI
    * make sure you check the box "Clone submodules" in the wizard
    * as you do not need to import the feature projects you can either set the scope of the wizard to the folder "bundles" in the "Working Tree" or you deselect these projects from the list (but importing the feature projects too will not do any harm)

### Run new Eclipse with Confidentiality Specification and Code Generation Support
* Run Palladio with the possibility to specifcy confidentiality to generate Java code with JML proof obligations for KeY
  * run a new "Eclipse Application" that is started from the Eclipse in which you checked out all plug-in projects as specified above
  * this can be done e.g. by creating a new debug configuration for an "Eclipse Application" with default settings).
    * Run - Debug Configurations - Eclipse Configuration - right click - New
* Create or modify a new or existing Palladio model in the workspace of the new Eclipse
  * e.g. import an existing project ("Import - Existing Projects into Workspace")
  * e.g. clone and import an example from the [Examples Repository](https://github.com/KASTEL-SCBS/Examples4SCBS)
  * e.g. right-click - New - Other - Palladio Modeling - New Palladio Project

### Create Architecture Model and Confidentiality Specification in Eclipse
* To model the component repository, system assembly, resource environment, and allocation you can use the wizard and graphical or tree-based editors of Palladio
  * e.g. using "New - Palladio Modeling - PCM ... Diagram"
* To add a confidentiality specification to the Palladio models you have to use the tree-based editor
  * open the tree-based editor by opening, for example, a .repository file with a double-click or right-click "Open with - Repository Model Editor"
  * right-click on the root element of a Palladio model (i.e. a "Repository", "System", "Resource Environment", or "Allocation" element)
  * Select "MDSD Profiles - Apply/Unapply Profiles" and add "Profile PCMConfidentialityProfile" in the dialog
  * Select the model element to which a confidentiality specification shall be added, right-click "MDSD Profiles - Apply/Unapply Stereotypes" and add the appropriate stereotype
  
### Generate Java Code to be completed and verified
* Select a repository model
* Right-click "KASTEL Code Analysis - Create Java Code for KeY"
