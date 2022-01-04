/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.icalendarfx.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.icalendarfx.parameters.CommonName;

public class CommonNameTest
{
    @Test // tests String as value
    public void canParseCommonName()
    {
        CommonName parameter = CommonName.parse("CN=David Bal");
        String expectedContent = "CN=David Bal";
        assertEquals(expectedContent, parameter.toString());
    }
    
    @Test // tests String as value
    public void canParseCommonName2()
    {
        CommonName parameter = CommonName.parse("CN=\"John Smith\"");
        String expectedContent = "CN=John Smith";
        assertEquals(expectedContent, parameter.toString());
    }
    
    @Test
    public void canMakeEmptyCommonName()
    {
    	CommonName parameter = new CommonName();
    	assertEquals("CN=", parameter.toString());
    }
}
