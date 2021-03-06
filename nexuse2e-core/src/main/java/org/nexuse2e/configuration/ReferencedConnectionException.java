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
package org.nexuse2e.configuration;

import java.util.Collection;

import org.nexuse2e.pojo.ParticipantPojo;

/**
 * This exception shall be thrown if an attempt is made to remove a connection from a configuration
 * that is still referenced by one or more participants.
 *
 * @author Jonas Reese
 */
public class ReferencedConnectionException extends ReferencedObjectException {

    private static final long serialVersionUID = 1L;

    private Collection<ParticipantPojo> referringParticipants;
    
    public ReferencedConnectionException( Collection<ParticipantPojo> referringParticipants ) {
        this.referringParticipants = referringParticipants;
    }

    @Override
    public Collection<ParticipantPojo> getReferringObjects() {
        return referringParticipants;
    }
}
