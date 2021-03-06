/**
 *  NEXUSe2e Business Messaging Open Source
 *  Copyright 2000-2021, direkt gruppe GmbH
 *
 *  This is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation version 3 of
 *  the License.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this software; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *  02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.nexuse2e.messaging;

import org.nexuse2e.BeanStatus;
import org.nexuse2e.Layer;
import org.nexuse2e.NexusException;
import org.nexuse2e.configuration.EngineConfiguration;

abstract public class AbstractPipeline implements Pipeline {

    protected boolean          frontendPipeline;
    protected boolean          outboundPipeline;
    protected BeanStatus       status           = BeanStatus.UNDEFINED;
    /**
     * The pipelets used for processing the message.
     */
    protected Pipelet[]        forwardPipelets  = null;

    /**
     * The pipelets used for processing an optionally synchronously created return message in frontend pipelines.
     */
    protected Pipelet[]        returnPipelets   = null;

    /**
     * The endpoint of this pipeline handling the message when this pipeline is done processing it.
     */
    protected MessageProcessor pipelineEndpoint = null;
    protected MessageProcessor returnPipelineEndpoint = null;

    abstract public MessageContext processMessage( MessageContext messageContext ) throws IllegalArgumentException,
            IllegalStateException, NexusException;

    public MessageProcessor getPipelineEndpoint() {

        return pipelineEndpoint;
    }

    public void setPipelineEndpoint( MessageProcessor pipelineEndpoint ) {

        if (pipelineEndpoint instanceof Pipelet) {
            ((Pipelet) pipelineEndpoint).setPipeline( this );
        }
        
        this.pipelineEndpoint = pipelineEndpoint;
    }

    public MessageProcessor getReturnPipelineEndpoint() {

        return returnPipelineEndpoint;
    }

    public void setReturnPipelineEndpoint( MessageProcessor returnPipelineEndpoint ) {

        if (returnPipelineEndpoint instanceof Pipelet) {
            ((Pipelet) returnPipelineEndpoint).setPipeline( this );
        }
        
        this.returnPipelineEndpoint = returnPipelineEndpoint;
    }

    /**
     * @return the forwardPipeline
     */
    public MessageProcessor[] getForwardPipelets() {

        return forwardPipelets;
    }

    /**
     * @param forwardPipeline the forwardPipeline to set
     */
    public void setForwardPipelets( Pipelet[] forwardPipelets ) {

        if (forwardPipelets != null) {
            for (Pipelet pipelet : forwardPipelets) {
                if (pipelet != null) {
                    pipelet.setPipeline( this );
                }
            }
        }
        
        this.forwardPipelets = forwardPipelets;
    }

    /**
     * @return the returnPipeline
     */
    public MessageProcessor[] getReturnPipelets() {

        return returnPipelets;
    }

    /**
     * @param returnPipeline the returnPipeline to set
     */
    public void setReturnPipelets( Pipelet[] returnPipelets ) {

        if (returnPipelets != null) {
            for (Pipelet pipelet : returnPipelets) {
                if (pipelet != null) {
                    pipelet.setPipeline( this );
                }
            }
        }

        this.returnPipelets = returnPipelets;
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.Manageable#activate()
     */
    public void activate() {

        status = BeanStatus.ACTIVATED;

        if ( forwardPipelets != null ) {
            for ( int i = 0; i < forwardPipelets.length; i++ ) {
                if ( forwardPipelets[i] != null ) {
                    forwardPipelets[i].activate();
                }
            }
        }
        if ( returnPipelets != null ) {
            for ( int i = 0; i < returnPipelets.length; i++ ) {
                if ( returnPipelets[i] != null ) {
                    returnPipelets[i].activate();
                }
            }
        }
        if ( getPipelineEndpoint() != null ) {
            ( (Pipelet) getPipelineEndpoint() ).activate();
        }
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.Manageable#deactivate()
     */
    public void deactivate() {

        status = BeanStatus.INITIALIZED;

        if ( forwardPipelets != null ) {
            for ( int i = 0; i < forwardPipelets.length; i++ ) {
                if ( forwardPipelets[i] != null ) {
                    forwardPipelets[i].deactivate();
                }
            }
        }
        if ( returnPipelets != null ) {
            for ( int i = 0; i < returnPipelets.length; i++ ) {
                if ( returnPipelets[i] != null ) {
                    returnPipelets[i].deactivate();
                }
            }
        }
        if ( getPipelineEndpoint() != null ) {
            ( (Pipelet) getPipelineEndpoint() ).deactivate();
        }
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.Manageable#getActivationRunlevel()
     */
    public Layer getActivationLayer() {

        if ( isOutboundPipeline() ) {
            return Layer.OUTBOUND_PIPELINES;
        }
        return Layer.INBOUND_PIPELINES;
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.Manageable#getStatus()
     */
    public BeanStatus getStatus() {

        try {
            BeanStatus minimumStatus = BeanStatus.STARTED;
            if ( forwardPipelets != null ) {
                for ( int i = 0; i < forwardPipelets.length; i++ ) {
                    if ( forwardPipelets[i] == null ) {
                        return BeanStatus.ERROR;
                    }
                    if ( forwardPipelets[i].getStatus().getValue() < minimumStatus.getValue() ) {
                        minimumStatus = forwardPipelets[i].getStatus();
                    }
                }
            }
            if ( returnPipelets != null ) {
                for ( int i = 0; i < returnPipelets.length; i++ ) {
                    if ( returnPipelets[i] == null ) {
                        return BeanStatus.ERROR;
                    }
                    if ( returnPipelets[i].getStatus().getValue() < minimumStatus.getValue() ) {
                        minimumStatus = returnPipelets[i].getStatus();
                    }
                }
            }
            if ( getPipelineEndpoint() == null ) {
                return BeanStatus.ERROR;
            }
            if ( getPipelineEndpoint() != null && getPipelineEndpoint() instanceof Pipelet ) {
                if ( ( (Pipelet) getPipelineEndpoint() ).getStatus().getValue() < minimumStatus.getValue() ) {
                    minimumStatus = ( (Pipelet) getPipelineEndpoint() ).getStatus();
                }
            }

            if ( ( forwardPipelets == null || forwardPipelets.length == 0 )
                    && ( returnPipelets == null || returnPipelets.length == 0 ) ) {
                minimumStatus = status;
            }

            return minimumStatus;
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return BeanStatus.UNDEFINED;
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.Manageable#initialize(org.nexuse2e.configuration.EngineConfiguration)
     */
    public void initialize( EngineConfiguration config ) throws InstantiationException {

        status = BeanStatus.INITIALIZED;

        if ( forwardPipelets != null ) {
            for ( int i = 0; i < forwardPipelets.length; i++ ) {
                if ( forwardPipelets[i] != null ) {
                    forwardPipelets[i].initialize( config );
                }
            }
        }
        if ( returnPipelets != null ) {
            for ( int i = 0; i < returnPipelets.length; i++ ) {
                if ( returnPipelets[i] != null ) {
                    returnPipelets[i].initialize( config );
                }
            }
        }
        if ( ( getPipelineEndpoint() != null ) && !( getPipelineEndpoint() instanceof BackendOutboundDispatcher )
                && !( getPipelineEndpoint() instanceof FrontendInboundDispatcher ) && !( getPipelineEndpoint() instanceof MessageHandlingCenter ) ) {
            ( (Pipelet) getPipelineEndpoint() ).initialize( config );
        }
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.Manageable#teardown()
     */
    public void teardown() {

        status = BeanStatus.INSTANTIATED;

        if ( forwardPipelets != null ) {
            for ( int i = 0; i < forwardPipelets.length; i++ ) {
                if ( forwardPipelets[i] != null ) {
                    forwardPipelets[i].teardown();
                }
            }
        }
        if ( returnPipelets != null ) {
            for ( int i = 0; i < returnPipelets.length; i++ ) {
                if ( returnPipelets[i] != null ) {
                    returnPipelets[i].teardown();
                }
            }
        }
        if ( (getPipelineEndpoint() != null) && !( getPipelineEndpoint() instanceof BackendOutboundDispatcher )
                && !( getPipelineEndpoint() instanceof FrontendInboundDispatcher )  && !( getPipelineEndpoint() instanceof MessageHandlingCenter ) ) {
            ( (Pipelet) getPipelineEndpoint() ).teardown();
        }
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.messaging.Pipeline#isFrontendPipeline()
     */
    public boolean isFrontendPipeline() {

        return frontendPipeline;
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.messaging.Pipeline#isOutbountPipeline()
     */
    public boolean isOutboundPipeline() {

        return outboundPipeline;
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.messaging.Pipeline#setFrontendPipeline(boolean)
     */
    public void setFrontendPipeline( boolean isFrontendPipeline ) {

        frontendPipeline = isFrontendPipeline;
    }

    /* (non-Javadoc)
     * @see org.nexuse2e.messaging.Pipeline#setOutboundPipeline(boolean)
     */
    public void setOutboundPipeline( boolean isOutboundPipeline ) {

        outboundPipeline = isOutboundPipeline;
    }

}
