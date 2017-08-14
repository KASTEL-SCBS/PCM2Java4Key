package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import edu.kit.kastel.scbs.javaAnnotations2JML.exception.ParseException;
import edu.kit.kastel.scbs.javaAnnotations2JML.parser.TopLevelTypesParser;

public class ParseTopLevelType implements Command {

    private TopLevelTypeProvider provider;

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
