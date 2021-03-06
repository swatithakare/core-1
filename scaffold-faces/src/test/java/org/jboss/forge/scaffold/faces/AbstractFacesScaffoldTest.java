/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.forge.scaffold.faces;

import org.jboss.forge.project.Project;
import org.jboss.forge.test.AbstractShellTest;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */

public abstract class AbstractFacesScaffoldTest extends AbstractShellTest
{
   //
   // Protected methods
   //

   protected Project setupScaffoldProject() throws Exception
   {
      Project project = initializeJavaProject();
      queueInputLines("HIBERNATE", "JBOSS_AS7", "", "");
      getShell().execute("persistence setup");
      queueInputLines("", "", "", "");
      getShell().execute("scaffold setup");
      return project;
   }

   protected Project setupScaffoldProject(String targetDir) throws Exception
   {
      Project project = initializeJavaProject();
      queueInputLines("HIBERNATE", "JBOSS_AS7", "", "");
      getShell().execute("persistence setup");
      queueInputLines("", "", "");
      getShell().execute("scaffold setup --targetDir " + targetDir);
      return project;
   }
   
   protected String normalized(StringBuilder sb) {
      return normalized(sb.toString());
   }
   
   protected String normalized(String input) {
      return input.replaceAll("(\r|\n|\\s)+"," ");
   }
}
