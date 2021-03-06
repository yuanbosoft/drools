package org.drools.core.common;

import org.drools.core.beliefsystem.BeliefSet;
import org.drools.core.beliefsystem.ModedAssertion;
import org.drools.core.definitions.rule.impl.RuleImpl;
import org.drools.core.util.LinkedList;
import org.drools.core.spi.Activation;
import org.drools.core.spi.PropagationContext;
import org.kie.internal.runtime.beliefs.Mode;

public class TruthMaintenanceSystemHelper {

    public static void removeLogicalDependencies(final InternalFactHandle handle, final PropagationContext propagationContext ) {
        final BeliefSet beliefSet = handle.getEqualityKey().getBeliefSet();
        if ( beliefSet != null && !beliefSet.isEmpty() ) {
            beliefSet.cancel(propagationContext);
        }
    }
    
    public static void clearLogicalDependencies(final InternalFactHandle handle, final PropagationContext propagationContext ) {
        final BeliefSet beliefSet = handle.getEqualityKey().getBeliefSet();
        if ( beliefSet != null && !beliefSet.isEmpty() ) {
            beliefSet.clear(propagationContext);
        }
    }    
    
    
    public static <M extends ModedAssertion<M>> void removeLogicalDependencies(final Activation<M> activation,
                                                                               final PropagationContext context,
                                                                               final RuleImpl rule) {
        final LinkedList<LogicalDependency<M>> list = activation.getLogicalDependencies();
        if ( list == null || list.isEmpty() ) {
            return;
        }

        for ( LogicalDependency<M> node = list.getFirst(); node != null; node = node.getNext() ) {
            removeLogicalDependency( node, context );
        }
        activation.setLogicalDependencies( null );
    }

    public static <M extends ModedAssertion<M>> void removeLogicalDependency(final LogicalDependency<M> node,
                                                                             final PropagationContext context) {
        final BeliefSet<M> beliefSet = ( BeliefSet ) node.getJustified();
        beliefSet.getBeliefSystem().delete( node, beliefSet, context );
    }
}
