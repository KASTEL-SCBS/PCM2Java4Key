package edu.kit.kastel.scbs.javaAnnotations2JML.util;

import java.io.IOException;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

/**
 * Utility helper class for accessing the Java abstract syntax tree.
 * 
 * This includes the writing of comments to Java types or methods.
 * 
 * @author Nils Wilka
 * @version 1.0, 14.08.2017
 */
public final class JdtAstJmlUtil {

    /**
     * No instantiation for utility classes.
     */
    private JdtAstJmlUtil() {
        ////
    }

    /**
     * Adds the given string to the given {@code IType} at the top by using the abstract syntax
     * tree.
     * 
     * @param type
     *            The type to add the string to.
     * @param text
     *            The text to be added.
     * @throws JavaModelException
     *             thrown if accessing the {@code ICompilationUnit} of the given type or saving the
     *             changes causes it.
     * @throws IOException
     *             if applying the changes causes it.
     */
    // the java doc of CompilationUnit#types() specifies the type 'AbstractTypeDeclaration'
    @SuppressWarnings("unchecked")
    public static void addStringToAbstractType(final IType type, final String text)
            throws JavaModelException, IOException {
        CompilationUnit cu = setupParserAndGetCompilationUnit(type.getCompilationUnit());

        // Create rewrite for modifying the AST
        ASTRewrite rewrite = ASTRewrite.create(cu.getAST());

        // get elements to change
        AbstractTypeDeclaration atd = findAbstractTypeDeclarationFromIType(cu.types(), type);

        // changes
        final ListRewrite listRewrite = rewrite.getListRewrite(atd, atd.getBodyDeclarationsProperty());
        insertStringIntoListRewrite(rewrite, listRewrite, text);

        writeBackOfChanges(type.getCompilationUnit(), rewrite);
    }

    /**
     * Finds the {@code AbstractTypeDeclaration} corresponding to the given {@code IType} in the
     * given list.
     * 
     * @param atdList
     *            The list to be searched in.
     * @param type
     *            The corresponding type to search the {@code AbstractTypeDeclaration} for.
     * @return The {@code AbstractTypeDeclaration} the given {@code IType} corresponds to.
     */
    public static AbstractTypeDeclaration findAbstractTypeDeclarationFromIType(List<AbstractTypeDeclaration> atdList,
            IType type) {
        for (AbstractTypeDeclaration atd : atdList) {
            ITypeBinding tb = atd.resolveBinding();
            IType je = (IType) tb.getJavaElement();
            if (je.equals(type)) {
                return atd;
            }
        }
        throw new TypeNotPresentException(
                "IType " + type.getElementName() + "not found in given list of AbstractTypeDeclarations", null);
    }

    /**
     * Creates an abstract syntax tree parser for a compilation unit and creates the compilation ast
     * node.
     * 
     * @param unit
     *            The {@code ICompilationUnit} to create the {@code CompilationUnit} ast node from.
     * @return The {@code CompilationUnit} corresponding to the given {@code ICompilationUnit}.
     */
    public static CompilationUnit setupParserAndGetCompilationUnit(final ICompilationUnit unit) {
        // Setup Parser with source code
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(unit);
        parser.setResolveBindings(true);
        CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        return cu;
    }

    /**
     * Applies the changes given by the {@code ASTRewrite} to the source code given by the
     * {@code ICompilationUnit}.
     * 
     * @param unit
     *            The source code to apply the changes to.
     * @param rewrite
     *            Contains the changes to be applied.
     * @throws JavaModelException
     *             thrown if accessing the {@code ICompilationUnit} or saving the changes causes it.
     * @throws IOException
     *             thrown if applying the changes to the rewrite causes it.
     */
    private static void writeBackOfChanges(final ICompilationUnit unit, ASTRewrite rewrite)
            throws JavaModelException, IOException {
        Document document = new Document(unit.getSource());
        TextEdit textEdits = rewrite.rewriteAST(document, null);
        try {
            textEdits.apply(document);
        } catch (MalformedTreeException e) {
            throw new IOException("Could not write back changes.", e);
        } catch (BadLocationException e) {
            throw new IOException("Could not write back changes.", e);
        }
        unit.getBuffer().setContents(document.get());
        unit.save(null, true);
    }

    /**
     * Creates a placeholder for the given {@code ASTRewrite} and inserts the given string at first
     * place of the given {@code ListRewrite}.
     * 
     * @param rewrite
     *            The ast rewrite to create the string placeholder in.
     * @param listRewrite
     *            The list rewrite to add the string to.
     * @param text
     *            The string to add.
     */
    private static void insertStringIntoListRewrite(ASTRewrite rewrite, final ListRewrite listRewrite, String text) {
        ASTNode placeHolder = rewrite.createStringPlaceholder(text, ASTNode.EMPTY_STATEMENT);
        listRewrite.insertFirst(placeHolder, null);
    }
}
