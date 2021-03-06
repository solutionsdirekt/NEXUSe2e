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
package org.nexuse2e;


public class ClusterException extends NexusException {

    /**
     * 
     */
    private static final long serialVersionUID = -7308656795219872919L;
    int responseCode = -1;
    public ClusterException( String message, int responseCode ) {

        super( message );
        this.responseCode = responseCode;
    }
    
    /**
     * @return the responseCode
     */
    public int getResponseCode() {
    
        return responseCode;
    }
    
    /**
     * @param responseCode the responseCode to set
     */
    public void setResponseCode( int responseCode ) {
    
        this.responseCode = responseCode;
    }
    
}
