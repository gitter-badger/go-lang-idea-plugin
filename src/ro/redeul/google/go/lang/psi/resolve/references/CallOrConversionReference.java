package ro.redeul.google.go.lang.psi.resolve.references;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.NotNull;
import ro.redeul.google.go.lang.psi.GoPsiElement;
import ro.redeul.google.go.lang.psi.expressions.primary.GoLiteralExpression;
import ro.redeul.google.go.lang.psi.processors.ResolveStates;
import ro.redeul.google.go.lang.psi.resolve.GoResolveResult;
import ro.redeul.google.go.lang.psi.resolve.MethodOrTypeNameSolver;
import ro.redeul.google.go.lang.psi.utils.GoPsiScopesUtil;

import java.util.ArrayList;
import java.util.List;

import static ro.redeul.google.go.util.LookupElementUtil.createLookupElement;

public class CallOrConversionReference extends AbstractCallOrConversionReference<CallOrConversionReference> {

    public CallOrConversionReference(GoLiteralExpression expression) {
        super(expression, RESOLVER);
    }

    private static final ResolveCache.AbstractResolver<CallOrConversionReference, GoResolveResult> RESOLVER =
            new ResolveCache.AbstractResolver<CallOrConversionReference, GoResolveResult>() {
                @Override
                public GoResolveResult resolve(@NotNull CallOrConversionReference psiReference, boolean incompleteCode) {
                    MethodOrTypeNameSolver<CallOrConversionReference> processor =
                            new MethodOrTypeNameSolver<CallOrConversionReference>(psiReference);

                    GoLiteralExpression expression = psiReference.getElement();
                    GoPsiScopesUtil.treeWalkUp(
                            processor,
                            expression, expression.getContainingFile(),
                            ResolveStates.initial());

                    return GoResolveResult.fromElement(processor.getChildDeclaration());
                }
            };

    @Override
    protected CallOrConversionReference self() {
        return this;
    }

    @NotNull
    @Override
    public Object[] getVariants() {

        GoLiteralExpression expression = getElement();

        final List<LookupElementBuilder> variants = new ArrayList<LookupElementBuilder>();

        MethodOrTypeNameSolver<CallOrConversionReference> processor =
                new MethodOrTypeNameSolver<CallOrConversionReference>(this) {
                    @Override
                    protected boolean addTarget(PsiElement declaration, PsiElement child) {
                        String name = PsiUtilCore.getName(child);

//                        String visiblePackageName =
//                                getState().get(ResolveStates.VisiblePackageName);

                        String visiblePackageName = null;

                        if (visiblePackageName != null) {
                            name = "".equals(visiblePackageName) ?
                                    name : visiblePackageName + "." + name;
                        }
                        if (name == null) {
                            return true;
                        }

                        GoPsiElement goPsi = (GoPsiElement) declaration;
                        GoPsiElement goChildPsi = (GoPsiElement) child;
                        variants.add(createLookupElement(goPsi, name, goChildPsi));
                        return true;
                    }
                };

        GoPsiScopesUtil.treeWalkUp(
                processor,
                expression, expression.getContainingFile(),
                ResolveStates.initial());

        return variants.toArray();
    }
}
