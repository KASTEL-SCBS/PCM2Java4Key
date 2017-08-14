package edu.kit.kastel.scbs.javaAnnotations2JML.command;

import java.util.List;

import edu.kit.kastel.scbs.javaAnnotations2JML.type.TopLevelType;

public interface TopLevelTypeProvider {

    public List<TopLevelType> getTopLevelTypes();
}
