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
package org.nexuse2e.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.exception.LockAcquisitionException;
import org.junit.Test;

public class InterceptorTest {
	@Test
	public void invocationThrowsLockAcquisitionException() throws Throwable {

		MethodInvocation invocationMock = mock(MethodInvocation.class);
		when(invocationMock.proceed()).thenThrow(
				new RuntimeException("Message", new LockAcquisitionException(
						"bla", new SQLException())));
		LockAcquisitionInterceptor interceptor = new LockAcquisitionInterceptor();
		interceptor.setTimeout(0);
		interceptor.setRetries(3);
		try {
			interceptor.invoke(invocationMock);
			fail( "Exception expected" );
		} catch (Exception e) {
			
		}
		verify(invocationMock, times(4)).proceed();

	}

	@Test
	public void invocationThrowsNoException() throws Throwable {

		MethodInvocation invocationMock = mock(MethodInvocation.class);
		when(invocationMock.proceed()).thenReturn(new Object());
		LockAcquisitionInterceptor interceptor = new LockAcquisitionInterceptor();
		interceptor.setTimeout(0);
		interceptor.setRetries(3);
		try {
			interceptor.invoke(invocationMock);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		verify(invocationMock, times(1)).proceed();
	}

	public void invocationThrowsNoLockAcquisitionException() throws Throwable {

		MethodInvocation invocationMock = mock(MethodInvocation.class);
		when(invocationMock.proceed()).thenThrow(
				new RuntimeException("Message", new SQLException() ) );
		LockAcquisitionInterceptor interceptor = new LockAcquisitionInterceptor();
		interceptor.setTimeout(0);
		interceptor.setRetries(3);
		try {
			interceptor.invoke(invocationMock);
			fail( "Exception expected" );
		} catch (Exception e) {
			assertTrue( e instanceof RuntimeException );
			assertEquals( e.getMessage(), "Message" );
			assertTrue( e.getCause() instanceof SQLException );
		}
		verify(invocationMock, times(1)).proceed();

	}
	
	@Test
	public void invocationThrowsExceptionNoRetries() throws Throwable {

		MethodInvocation invocationMock = mock(MethodInvocation.class);
		when(invocationMock.proceed()).thenThrow(
				new RuntimeException("Message", new LockAcquisitionException(
						"bla", new SQLException())));
		LockAcquisitionInterceptor interceptor = new LockAcquisitionInterceptor();
		interceptor.setTimeout(0);
		interceptor.setRetries(0);
		try {
			interceptor.invoke(invocationMock);
			fail( "Exception expected" );
		} catch (Exception e) {
			assertTrue( e instanceof RuntimeException );
			assertEquals( e.getMessage(), "Message" );
			assertTrue( e.getCause() instanceof LockAcquisitionException );
		}
		verify(invocationMock, times(1)).proceed();

	}
}
