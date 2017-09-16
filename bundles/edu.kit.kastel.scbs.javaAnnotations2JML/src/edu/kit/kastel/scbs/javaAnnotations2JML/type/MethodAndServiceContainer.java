package edu.kit.kastel.scbs.javaAnnotations2JML.type;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.DataSet;
import edu.kit.kastel.scbs.javaAnnotations2JML.confidentiality.InformationFlowAnnotation;
import edu.kit.kastel.scbs.javaAnnotations2JML.type.service.Service;

/**
 * Wrapper for an {@code IType} from an either an {@code ICompilationUnit} or a binary class. Does
 * not parse its given {@code IType} and therefore does not provide direct access to the values of
 * its {@code IType}. Its {@code IType} can nevertheless be accessed by
 * {@code TopLevelType#getIType}.
 * 
 * The values of the {@code IType} must be parsed and added to this type, before they can be used.
 * This includes the source {@code IMethod}s, {@code TopLevelType.Field}s, super
 * {@code TopLevelType}s, parsed {@code IMethod}s with their {@code InformationFlowAnnotation}s and
 * lastly the {@code AbstractServiceType}s.
 * 
 * A {@code TopLevelType} should only consist of values from its corresponding {@code IType}.
 * 
 * @see IType
 * @see IMethod
 * @see InformationFlowAnnotation
 * 
 * @author Nils Wilka
 * @version 1.0, 15.09.2017
 */
public class MethodAndServiceContainer implements MethodAndAnnotationPairProvider, ServiceProvider {

    private IType javaType;

    private List<IMethod> sourceMethods;

    private Map<IMethod, InformationFlowAnnotation> methods;

    private Map<DataSet, List<Service>> services;

    /**
     * Creates a Wrapper for an {@code IType} from an either an {@code ICompilationUnit} or a binary
     * class. Keep in mind, that it does not parse its given {@code IType} and therefore does not
     * provide direct access to the values of its {@code IType}. They must be manually added first.
     * 
     * @param type
     *            The wrapped {@code IType} from an either an {@code ICompilationUnit} or a binary
     *            class.
     */
    public MethodAndServiceContainer(IType type) {
        javaType = type;
        methods = new HashMap<>();
        sourceMethods = new LinkedList<>();
    }

    /**
     * Gets the name of this {@code TopLevelType}, which is exactly the same as the one from its
     * {@code IType}.
     * 
     * @return The name of this {@code TopLevelType}.
     */
    public String getName() {
        return javaType.getElementName();
    }

    /**
     * Gets the wrapped {@code IType}, e.g. for parsing purposes. This class should only provide
     * information about this specific {@code IType}.
     * 
     * @return The wrapped {@code IType}.
     */
    public IType getIType() {
        return javaType;
    }

    /**
     * Adds an {@code IMethod} and its {@code InformationFlowAnnotation}.
     * 
     * @param method
     *            The method to add.
     * @param annotation
     *            The information flow annotation to add.
     */
    public void addMethodAndAnnotationPair(IMethod method, InformationFlowAnnotation annotation) {
        methods.put(method, annotation);
    }

    @Override
    public Map<IMethod, InformationFlowAnnotation> getMethodAndAnnotationsPairs() {
        return methods;
    }

    /**
     * Adds a method without an information flow annotation.
     * 
     * @param method
     *            The method without an information flow annotation.
     */
    public void addMethod(IMethod method) {
        this.sourceMethods.add(method);
    }

    /**
     * Gets the list of {@code IMethod}s without information flow annotations.
     * 
     * @return The list of {@code IMethod}s without information flow annotations.
     */
    public List<IMethod> getMethods() {
        return this.sourceMethods;
    }

    /**
     * Adds the given data set to services map to this container.
     * 
     * @param services
     *            The data set to services map to add.
     */
    public void addService(Map<DataSet, List<Service>> services) {
        this.services = services;
    }

    @Override
    public Set<DataSet> getDataSets() {
        Optional<Set<DataSet>> optional = Optional.ofNullable(services.keySet());
        return optional.orElse(new HashSet<>());
    }

    @Override
    public List<Service> getServices() {
        List<Service> list = new LinkedList<>();
        services.values().forEach(list::addAll);
        return list;
    }

    @Override
    public List<Service> getServices(DataSet dataSet) {
        Optional<List<Service>> optional = Optional.ofNullable(services.get(dataSet));
        return optional.orElse(new LinkedList<Service>());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof MethodAndServiceContainer) {
            MethodAndServiceContainer other = (MethodAndServiceContainer) obj;
            // equal by name
            return this.javaType.getElementName().equals(other.javaType.getElementName());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return javaType.hashCode();
    }

    @Override
    public String toString() {
        return javaType.toString();
    }

    /**
     * Creates multiple {@code TopLevelType}s with the given {@code IType}s.
     * 
     * @param types
     *            The {@code IType}s to create each {@code TopLevelType} from.
     * @return The newly created top level types.
     */
    public static List<MethodAndServiceContainer> create(List<IType> types) {
        return types.stream().map(e -> create(e)).collect(Collectors.toList());
    }

    /**
     * Create a new {@code TopLevelType} with the given {@code IType}.
     * 
     * @param type
     *            The {@code IType} to create the {@code TopLevelType} from.
     * @return The newly created top level type.
     */
    public static MethodAndServiceContainer create(IType type) {
        return new MethodAndServiceContainer(type);
    }
}
