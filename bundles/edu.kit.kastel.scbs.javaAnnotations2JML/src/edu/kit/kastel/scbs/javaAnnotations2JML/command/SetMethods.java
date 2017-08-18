package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.HashSet;
import java.util.Set;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.AbstractServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.MethodParser;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

/**
 * Command for setting the methods of service types and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public class SetMethods implements Command {

    private ServiceTypeProvider provider;

    private ConfidentialitySpecificationProvider specProvider;

    /**
     * Creates a new method setter command with the given service type provider and confidentiality
     * specification provider.
     * 
     * @param provider
     *            The provider of service types.
     * 
     * @param specificationProvider
     *            The provider of the confidentiality specification.
     */
    public SetMethods(ServiceTypeProvider provider, ConfidentialitySpecificationProvider specificationProvider) {
        this.provider = provider;
        this.specProvider = specificationProvider;
    }

    @Override
    public void execute() {
        // unique set
        Set<TopLevelType> serviceTypeTopLevelTypes = new HashSet<>();
        for (AbstractServiceType serviceType : provider.getServiceTypes()) {
            serviceTypeTopLevelTypes.add(serviceType.getType());
        }

        for (TopLevelType topLevelType : serviceTypeTopLevelTypes) {
            MethodParser methodParser = new MethodParser(specProvider.getConfidentialitySpecification(), topLevelType,
                    topLevelType);
            try {
                methodParser.parse();
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
