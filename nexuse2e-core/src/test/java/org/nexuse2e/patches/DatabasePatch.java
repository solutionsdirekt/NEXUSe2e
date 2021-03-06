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
package org.nexuse2e.patches;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.nexuse2e.Engine;
import org.nexuse2e.NexusException;
import org.nexuse2e.dao.TransactionDAO;
import org.nexuse2e.patch.PatchReporter;




public abstract class DatabasePatch {

    protected PatchReporter patchReporter = null;
    
    /**
     * @return
     */
    protected Session getDBSession() {
        
        if(Engine.getInstance() == null) {
            report("engine not initialized");
            return null;
        }
        TransactionDAO dao;
        try {
            dao = (TransactionDAO) Engine.getInstance().getBeanFactory().getBean( "transactionDao" );
            if(dao == null) {
                report("no config dao available");
                return null;
            }
        } catch ( Exception e ) {
            report("Exception: "+e.getMessage());
            return null;
        }
        return dao.getDBSession();
    }
    
    /**
     * @param session
     */
    protected void releaseDBSession(Session session) {
        if(Engine.getInstance() == null) {
            report("engine not initialized");
            return;
        }
        TransactionDAO dao;
        try {
            dao = (TransactionDAO) Engine.getInstance().getBeanFactory().getBean( "transactionDao" );
            if(dao == null) {
                report("no config dao available");
                return;
            }
        } catch ( Exception e ) {
            report("Exception: "+e.getMessage());
            return;
        }
        dao.releaseDBSession( session );
    }
    
    protected boolean isIndexPresent(String indexName,String tableName,String columnName, Session session) throws NexusException {
        boolean sessionRequested = false;
        
        if(session == null) {
            session = getDBSession();
            if(session == null) {
                throw new NexusException("no session available");
            }
            sessionRequested = true;
            
        }
        
        IndexWorker indexWork = new IndexWorker();
        try {
            indexWork.indexName = indexName;
            indexWork.tableName = tableName;
            indexWork.columnName = columnName;
            
        	session.doWork(indexWork);
            
        } catch ( Exception e ) {
            throw new NexusException("error while receiving connection: "+e.getMessage(),e);
        }
        if(sessionRequested) {
            releaseDBSession( session );
        }
        if(!indexWork.indexNameFound && indexWork.indexColumnsFound) {
            throw new NexusException("index for specified columns found but different index name");
        }
        return indexWork.indexNameFound;
    }
    
    protected void alterTable(String sql,String tablename,Session session) throws NexusException {
        boolean sessionRequested = false;
        if(session == null) {
            session = getDBSession();
            if(session == null) {
                throw new NexusException("no session available");
            }
            sessionRequested = true;
        }
        try {
            SQLQuery query = session.createSQLQuery( sql );
            query.executeUpdate();
        } catch ( HibernateException e ) {
            report("alter table: "+e.getMessage());
        }
        
        if(sessionRequested) {
            releaseDBSession( session );
        }
        
    }
    
    
    /**
     * @author gesch
     *
     */
    private class IndexWorker implements Work {
        
        public String indexName = "";
        public String tableName = "";
        public String columnName ="";
        public boolean indexNameFound = false;
        public boolean indexColumnsFound = false;
        
        public void execute( Connection connection ) throws SQLException {
            try {
                DatabaseMetaData metaData = null;
                metaData = connection.getMetaData();
                ResultSet result = metaData.getIndexInfo( null, null, tableName, false, false );
                String tempIndexName = "";
                Map<String, List<String>> indexMap = new HashMap<String, List<String>>();
                List<String> colList = new ArrayList<String>();
                while(result.next()) {
                    String name = result.getString( 6 );
                    if(name == null) {
                        continue;
                    }
                    String column = result.getString( 9 );
                    if(!name.equals( tempIndexName )) {
                        colList = new ArrayList<String>();
                        indexMap.put( name, colList );
                        tempIndexName = name;
                    }
                    colList.add( column );
                }
                if(indexMap.get( indexName )!= null) {
                    indexNameFound = true;
                }
                Collection<List<String>> cols = indexMap.values();
                Iterator<List<String>> i = cols.iterator();
                
                while(i.hasNext()){
                    String tempColumns = "";
                    List<String> index = i.next();
                    for ( String col : index ) {
                        tempColumns += (tempColumns.length() == 0 ? col:","+col );
                    }
                    System.out.println("index: "+columnName+"/"+tempColumns);
                    if(columnName.equals( tempColumns )){
                        indexColumnsFound = true;
                    }
                }
                
            } catch ( RuntimeException e ) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    
    /**
     * @param patchReporter
     */
    public final void setPatchReporter( PatchReporter patchReporter ) {
        this.patchReporter = patchReporter;
    }
    
    /**
     * @param message
     */
    private void report(String message) {
        if(patchReporter != null) {
            patchReporter.info( message );
        }
    }
    
}
