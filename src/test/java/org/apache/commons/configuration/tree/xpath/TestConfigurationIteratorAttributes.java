/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.configuration.tree.xpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.configuration.tree.ImmutableNode;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@code ConfigurationNodeIteratorAttributes}.
 *
 * @version $Id$
 */
public class TestConfigurationIteratorAttributes extends AbstractXPathTest
{
    /** Constant for the name of another test attribute.*/
    private static final String TEST_ATTR = "test";

    /** Stores the node pointer of the test node.*/
    private ConfigurationNodePointer<ImmutableNode> pointer;

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();

        // Adds further attributes to the test node
        ImmutableNode orgNode = root.getChildren().get(1);
        ImmutableNode testNode = orgNode.setAttribute(TEST_ATTR, "yes");
        pointer =
                new ConfigurationNodePointer<ImmutableNode>(testNode,
                        Locale.getDefault(), handler);
    }

    /**
     * Tests to iterate over all attributes.
     */
    @Test
    public void testIterateAllAttributes()
    {
        ConfigurationNodeIteratorAttribute<ImmutableNode> it =
                new ConfigurationNodeIteratorAttribute<ImmutableNode>(pointer,
                        new QName(null, "*"));
        assertEquals("Wrong number of attributes", 2, iteratorSize(it));
        List<NodePointer> attrs = iterationElements(it);
        Set<String> attrNames = new HashSet<String>();
        for (NodePointer np : attrs)
        {
            attrNames.add(np.getName().getName());
        }
        assertTrue("First attribute not found", attrNames.contains(ATTR_NAME));
        assertTrue("Second attribute not found", attrNames.contains(TEST_ATTR));
    }

    /**
     * Tests to iterate over attributes with a specific name.
     */
    @Test
    public void testIterateSpecificAttribute()
    {
        ConfigurationNodeIteratorAttribute<ImmutableNode> it =
                new ConfigurationNodeIteratorAttribute<ImmutableNode>(pointer,
                        new QName(null, TEST_ATTR));
        assertEquals("Wrong number of attributes", 1, iteratorSize(it));
        assertEquals("Wrong attribute", TEST_ATTR, iterationElements(it).get(0)
                .getName().getName());
    }

    /**
     * Tests to iterate over non existing attributes.
     */
    @Test
    public void testIterateUnknownAttribute()
    {
        ConfigurationNodeIteratorAttribute<ImmutableNode> it =
                new ConfigurationNodeIteratorAttribute<ImmutableNode>(pointer,
                        new QName(null, "unknown"));
        assertEquals("Found attributes", 0, iteratorSize(it));
    }

    /**
     * Tests iteration if a namespace is specified. This is not supported, so
     * the iteration should be empty.
     */
    @Test
    public void testIterateNamespace()
    {
        ConfigurationNodeIteratorAttribute<ImmutableNode> it =
                new ConfigurationNodeIteratorAttribute<ImmutableNode>(pointer,
                        new QName("test", "*"));
        assertEquals("Found attributes", 0, iteratorSize(it));
    }
}
