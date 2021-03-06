/*
 * JBoss, by Red Hat.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.forge.shell.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.test.command.MockOptionTestPlugin;
import org.jboss.forge.test.AbstractShellTest;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@RunWith(Arquillian.class)
public class ExecutionParserTest extends AbstractShellTest
{
   @Inject
   @Alias("motp")
   private MockOptionTestPlugin plugin;

   @Test
   public void testInvalidSuppliedOptionIsCorrected() throws Exception
   {
      String packg = "com.example.good.pkg";
      queueInputLines(packg);
      getShell().execute("motp suppliedOption --package bad_%package");
      assertEquals(packg, plugin.getSuppliedOption());
   }

   @Test
   public void testOmittedRequiredOptionIsFilled() throws Exception
   {
      String packg = "com.example.good.pkg";
      queueInputLines("another#$%bad($package", packg);
      getShell().execute("motp requiredOption");
      assertEquals(packg, plugin.getRequiredOption());
   }

   @Test
   public void testDefaultCommandPassedExecutedLiterallyTreatedAsArg() throws Exception
   {
      getShell().execute("motp motp");
      assertEquals("motp", plugin.getDefaultCommandArg());
   }

   @Test
   public void testOmittedOptionalBooleanDefaultsToFalse() throws Exception
   {
      assertNull(plugin.getBooleanOptionOmitted());
      getShell().execute("motp booleanOptionOmitted");
      assertEquals(false, plugin.getBooleanOptionOmitted());
   }

   @Test
   public void testVarargsTakesUnusedParameters() throws Exception
   {
      getShell().execute("motp varargsOption -Pfoo --bar -ext");
      assertEquals(3, plugin.getVarargsOptions().size());
      assertTrue(plugin.getVarargsOptions().contains("-Pfoo"));
      assertTrue(plugin.getVarargsOptions().contains("--bar"));
      assertTrue(plugin.getVarargsOptions().contains("-ext"));
   }
}
