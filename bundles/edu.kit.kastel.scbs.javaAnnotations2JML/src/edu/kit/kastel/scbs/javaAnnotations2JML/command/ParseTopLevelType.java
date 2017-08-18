package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.TopLevelTypesParser;

/**
 * Command for parsing top level types, i.e. setting their super types and fields and methods if
 * necessary and reacting to exceptions.
 * 
 * @author Nils Wilka
 * @version 1.0, 18.08.2017
 */
public class ParseTopLevelType implements Command {

    private TopLevelTypeProvider provider;

    /**
     * Creates a new top level type parsing command with the given top level type provider
     * 
     * @param provider
     *            The provider of a top level types.
     */
    public ParseTopLevelType(TopLevelTypeProvider provider) {
        this.provider = provider;
    }

    @Override
    public void execute() {
        TopLevelTypesParser topLevelTypesParser = new TopLevelTypesParser(provider.getTopLevelTypes());
        try {
            topLevelTypesParser.parse();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
