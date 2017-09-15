package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.List;
import java.util.function.Supplier;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.scanner.TopLevelTypesScanner;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

/**
 * Command for scanning top level types, i.e. setting their super types and fields and methods if
 * necessary and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.2, 14.09.2017
 */
public class ScanTopLevelTypesCommand extends Command {

    private Supplier<List<TopLevelType>> supplier;

    /**
     * Creates a new top level type parsing command with the given top level type supplier.
     * 
     * @param supplier
     *            The supplier of a top level types.
     */
    public ScanTopLevelTypesCommand(Supplier<List<TopLevelType>> supplier) {
        this.supplier = supplier;
    }

    @Override
    public void execute() {
        TopLevelTypesScanner topLevelTypesParser = new TopLevelTypesScanner(supplier.get());
        try {
            topLevelTypesParser.parse();
        } catch (ParseException e) {
            e.printStackTrace();
            abort();
        }
    }
}
