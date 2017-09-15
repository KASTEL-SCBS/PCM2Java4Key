package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.ConfidentialitySpecification;
import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.generation.serviceType.AbstractServiceType;
import edu.kit.kastel.scbs.javaAnnotations2JML.generator.MethodsAndInformationFlowPairsGenerator;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

/**
 * Command for setting the methods and their information flow annotations of service types and
 * reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.2, 14.09.2017
 */
public class GenerateMethodsAndInformationFlowPairsCommand extends Command {

    private Supplier<List<AbstractServiceType>> supplier;

    private Supplier<ConfidentialitySpecification> specificationSupplier;

    /**
     * Creates a new method setter command with the given service type supplier and confidentiality
     * specification supplier.
     * 
     * @param serviceTypeSupplier
     *            The supplier of service types.
     * @param specificationSupplier
     *            The supplier of the confidentiality specification.
     */
    public GenerateMethodsAndInformationFlowPairsCommand(Supplier<List<AbstractServiceType>> serviceTypeSupplier,
            Supplier<ConfidentialitySpecification> specificationSupplier) {
        this.supplier = serviceTypeSupplier;
        this.specificationSupplier = specificationSupplier;
    }

    @Override
    public void execute() {
        // unique set
        Set<TopLevelType> serviceTypeTopLevelTypes = new HashSet<>();
        for (AbstractServiceType serviceType : supplier.get()) {
            serviceTypeTopLevelTypes.add(serviceType.getType());
        }

        for (TopLevelType topLevelType : serviceTypeTopLevelTypes) {
            MethodsAndInformationFlowPairsGenerator methodParser;
            methodParser = new MethodsAndInformationFlowPairsGenerator(specificationSupplier.get(), topLevelType,
                    topLevelType);
            try {
                methodParser.parse();
            } catch (ParseException e) {
                e.printStackTrace();
                abort();
            }
        }
    }
}
