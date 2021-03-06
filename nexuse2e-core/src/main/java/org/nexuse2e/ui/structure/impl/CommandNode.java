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
package org.nexuse2e.ui.structure.impl;


/**
 * Defines the interface of a command node.
 * @author Sebastian Schulze
 * @date 29.11.2006
 */
public class CommandNode extends AbstractStructureNode {

    /**
     * Constructor.
     * @param target Target URL.
     * @param label Label of the node.
     * @param icon URL of the nodes icon.
     */
    public CommandNode( String target, String label, String icon ) {

        super( target, label, icon );
    }
    
    public CommandNode createCopy() {
        CommandNode cm = new CommandNode( target, label, icon );
        cm.setParentNode( parent );
        cm.setPattern( pattern );
        return cm;
    }
}
