package edu.kit.kastel.scbs.javaAnnotations2JML;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public final class JdtAstJmlUtil {

    /**
     * No instantiation for utility classes.
     */
    private JdtAstJmlUtil() {
        ////
    }

    /*
     * comment to method declaration (in abstract type declaration in compilation unit)
     */
    public static void addStringToMethod(final IType type, final IMethod method, final String text)
            throws JavaModelException {
        CompilationUnit cu = setupParserAndGetCompilationUnit(type.getCompilationUnit());

        // create rewrite for modifying the AST
        ASTRewrite rewrite = ASTRewrite.create(cu.getAST());

        // get elements to change
        AbstractTypeDeclaration atd = findAbstractTypeDeclarationFromIType(cu.types(), type);
        TypeDeclaration typeDeclaration = (TypeDeclaration) atd;
        List<MethodDeclaration> td = Arrays.asList(typeDeclaration.getMethods());
        MethodDeclaration methodDecl = findMethodDeclarationFromIMethod(td, method);

        // changes
        // hack: add comment as modifier + a line break
        final ListRewrite listRewrite = rewrite.getListRewrite(methodDecl, MethodDeclaration.MODIFIERS2_PROPERTY);
        insertStringIntoListRewrite(rewrite, listRewrite, text + "\n");

        writeBackOfChanges(type.getCompilationUnit(), rewrite);
    }

    /* comment to abstract type declaration (in compilation unit) */
    public static void addStringToAbstractType(final IType type, final IMethod method, final String text)
            throws JavaModelException {
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

    /* comment to compilation unit */
    public static void addStringToCompilationUnit(final CompilationUnit cu, final IMethod method, final String text)
            throws JavaModelException {
        // create rewrite for modifying the AST
        ASTRewrite rewrite = ASTRewrite.create(cu.getAST());

        // get elements to change
        final ListRewrite listRewrite = rewrite.getListRewrite(cu, CompilationUnit.TYPES_PROPERTY);
        insertStringIntoListRewrite(rewrite, listRewrite, text);

        ICompilationUnit unit = (ICompilationUnit) cu.getJavaElement(); // TODO
        writeBackOfChanges(unit, rewrite);
    }

    public static MethodDeclaration findMethodDeclarationFromIMethod(List<MethodDeclaration> mdList, IMethod method)
            throws JavaModelException {
        for (MethodDeclaration methodDeclaration : mdList) {
            if (method.isSimilar(getIMethodFromMethodDeclaration(method.getCompilationUnit(), methodDeclaration))) {
                return methodDeclaration;
            }
        }
        return null; // TODO
    }

    public static AbstractTypeDeclaration findAbstractTypeDeclarationFromIType(List<AbstractTypeDeclaration> atdList,
            IType type) throws JavaModelException {
        for (AbstractTypeDeclaration atd : atdList) {
            ITypeBinding tb = atd.resolveBinding();
            IType je = (IType) tb.getJavaElement();
            if (je.equals(type)) {
                return atd;
            }
        }
        return null; // TODO
    }

    public static IMethod getIMethodFromMethodDeclaration(ICompilationUnit unit, MethodDeclaration methodDeclaration)
            throws JavaModelException {
        return (IMethod) unit.getElementAt(methodDeclaration.getStartPosition());
    }

    public static CompilationUnit setupParserAndGetCompilationUnit(final ICompilationUnit unit) {
        // Setup Parser with source code
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(unit); // set source
        parser.setResolveBindings(true); // we need bindings later on
        CompilationUnit cu = (CompilationUnit) parser.createAST(null /* IProgressMonitor */); // parse
        return cu;
    }

    public static void writeBackOfChanges(final ICompilationUnit unit, ASTRewrite rewrite) throws JavaModelException {
        Document document = new Document(unit.getSource());
        TextEdit textEdits = rewrite.rewriteAST(document, null);
        try {
            textEdits.apply(document);
        } catch (MalformedTreeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        unit.getBuffer().setContents(document.get());
        unit.save(null, true);
    }

    private static void insertStringIntoListRewrite(ASTRewrite rewrite, final ListRewrite listRewrite, String comment) {
        ASTNode placeHolder = rewrite.createStringPlaceholder(comment, ASTNode.EMPTY_STATEMENT);
        listRewrite.insertFirst(placeHolder, null);
    }
}
