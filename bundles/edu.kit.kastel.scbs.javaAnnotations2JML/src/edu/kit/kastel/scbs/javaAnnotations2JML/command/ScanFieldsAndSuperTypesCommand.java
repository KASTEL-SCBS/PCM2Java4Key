package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.function.Supplier;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.scanner.FieldsAndSuperTypesScanner;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

/**
 * Command for scanning fields and super types, i.e. setting the super types and fields of the top
 * level types from their ITypes and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.3, 16.09.2017
 */
public class ScanFieldsAndSuperTypesCommand extends Command {

    private final Supplier<Iterable<TopLevelType>> supplier;

    /**
     * Creates a IType scanner for fields and super types command with the given top level type
     * supplier.
     * 
     * @param supplier
     *            The supplier for top level types.
     */
    public ScanFieldsAndSuperTypesCommand(final Supplier<Iterable<TopLevelType>> supplier) {
        this.supplier = supplier;
    }

    @Override
    public void execute() {
        final FieldsAndSuperTypesScanner scanner;
        scanner = new FieldsAndSuperTypesScanner(supplier.get());
        try {
            scanner.scan();
        } catch (ParseException e) {
            e.printStackTrace();
            abort();
        }
    }
}
