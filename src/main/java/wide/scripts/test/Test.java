package wide.scripts.test;

import java.util.Arrays;

import wide.core.framework.extensions.scripts.Script;
import wide.core.framework.storage.server.ServerStorage;
import wide.scripts.ScriptDefinition;

/**
 * Simple testing script, use this as playground.
 * Don't commit its content in the master branch!
 */
public class Test extends Script
{
    public Test(final ScriptDefinition definition)
    {
        super(definition);
    }

    @Override
    public void run(final String[] args)
    {
        System.out.println(String.format("Running %s script with args %s.",
                toString(), Arrays.toString(args)));

        usePlayground(args);
    }

    @Override
    public String getUsage()
    {
        return "";
    }

    // Playground begin (only commit it in sub-branches to test stuff!)
    private void usePlayground(final String[] args)
    {
        final ServerStorage<CreatureTemplate> template =
                new ServerStorage<>(CreatureTemplateStructure.class);

        template.get(41378);

        System.out.println(template);
    }
}
