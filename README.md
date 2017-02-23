# PCM2Java4Key
Generate Java code to verify Palladio models using KeY

Generate Java code from architecture models that were created using the Palladio Component Model (PCM) with proof obligations for verifying the confidentiality of data flows in the Java code after manual completion. 

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
