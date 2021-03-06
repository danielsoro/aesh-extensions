/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aesh.extensions.echo;

import java.io.IOException;
import java.util.List;

import org.jboss.aesh.cl.Arguments;
import org.jboss.aesh.cl.CommandDefinition;
import org.jboss.aesh.cl.Option;
import org.jboss.aesh.console.command.Command;
import org.jboss.aesh.console.command.CommandResult;
import org.jboss.aesh.console.command.invocation.CommandInvocation;
import org.jboss.aesh.terminal.Shell;

/**
 * A simple echo command.
 *
 * @author <a href="mailto:00hf11@gmail.com">Helio Frota</a>
 */
@CommandDefinition(name = "echo", description = "Echo the STRING(s) to standard output.")
public class Echo implements Command<CommandInvocation> {

    @Option(shortName = 'h', name = "help", hasValue = false, description = "display this help and exit")
    private boolean help;

    @Arguments
    private List<String> arguments;

    @Override
    public CommandResult execute(CommandInvocation commandInvocation) throws IOException {

        Shell shell = commandInvocation.getShell();

        if (help || arguments == null || arguments.isEmpty()) {
            shell.out().println(commandInvocation.getHelpInfo("echo"));
            return CommandResult.SUCCESS;
        }

        String stdout = "";
        for (String s : arguments) {
            stdout += s + " ";
        }
        stdout = stdout.trim();

        shell.out().println(stdout);

        return CommandResult.SUCCESS;
    }

}
