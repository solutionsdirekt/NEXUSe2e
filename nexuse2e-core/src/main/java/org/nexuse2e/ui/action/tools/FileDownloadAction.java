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
package org.nexuse2e.ui.action.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.nexuse2e.configuration.EngineConfiguration;
import org.nexuse2e.ui.action.NexusE2EAction;
import org.nexuse2e.util.FileUtil;

/**
 * Provides the model for the file download page. 
 * 
 * @author Jonas Reese
 * @version $LastChangedRevision:  $ - $LastChangedDate:  $ by $LastChangedBy:  $
 */
public class FileDownloadAction extends NexusE2EAction {

    @Override
    public ActionForward executeNexusE2EAction(
            ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response,
            EngineConfiguration engineConfiguration,
            ActionMessages errors,
            ActionMessages messages ) throws Exception {
        
                
        request.setAttribute( ATTRIBUTE_COLLECTION, getTuples() );
        
        return actionMapping.findForward( ACTION_FORWARD_SUCCESS );
    }
    
    public static List<Tuple> getTuples() {
        Map<String, String> configuration = FileDownloadConfiguration.getInstance().getConfiguration();
        List<Tuple> tuples = new ArrayList<Tuple>( configuration.size() );
        for (String path : configuration.keySet()) {
            if (path != null && path.length() > 0) {
                List<File> files = new ArrayList<File>();
                String leaf = FileUtil.getLeaf( path );
                if (FileUtil.containsDosStylePattern( leaf )) {
                    File dir = new File( path.substring( 0, path.length() - leaf.length() ) );
                    if (dir.exists() && dir.isDirectory()) {
                        for (File f : dir.listFiles( FileUtil.getFilenameFilterForPattern( leaf ) )) {
                            files.add( f );
                        }
                    }
                } else {
                    File f = new File( path );
                    if (f.exists()) {
                        if (f.isFile()) { // single file
                            files.add( f );
                        } else if (f.isDirectory()) { // directory
                            for (File file : f.listFiles()) {
                                if (file.exists() && file.isFile()) {
                                    files.add( file );
                                }
                            }
                        }
                        
                    }
                }
                String name = configuration.get( path );
                if (name == null) {
                    name = path;
                }
                tuples.add( new Tuple( name, files ) );
            }
        }
        return tuples;
    }

    public static class Tuple {
        String name;
        List<File> files;
        Tuple( String name, List<File> files ) {
            this.name = name;
            this.files = files;
        }
        public String getName() {
            return name;
        }
        public List<File> getFiles() {
            return files;
        }
    }
}
