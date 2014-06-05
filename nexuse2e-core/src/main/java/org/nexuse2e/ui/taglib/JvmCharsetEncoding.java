/**
 *  NEXUSe2e Business Messaging Open Source
 *  Copyright 2000-2009, Tamgroup and X-ioma GmbH
 *
 *  This is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation version 2.1 of
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
package org.nexuse2e.ui.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Tag that allows to print the JVM default charset encoding.
 *
 * @author Jonas Reese
 */
public class JvmCharsetEncoding extends BodyTagSupport {

    private static final long serialVersionUID = 4338460000631138498L;
    
    
    @Override
    public int doStartTag() throws JspException {

        try {
            pageContext.getOut().print(java.nio.charset.Charset.defaultCharset().name());
        } catch (IOException e) {
            throw new JspException(e);
        }
        
        return EVAL_BODY_INCLUDE;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {

        return EVAL_PAGE;
    }
}
