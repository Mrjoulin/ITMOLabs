package com.meterware.httpunit;
/********************************************************************************************************************
* $Id: HtmlErrorListener.java,v 1.2 2002/12/25 15:23:11 russgold Exp $
*
* Copyright (c) 2001-2002, Russell Gold
*
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
* documentation files (the "Software"), to deal in the Software without restriction, including without limitation
* the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
* to permit persons to whom the Software is furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all copies or substantial portions
* of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
* THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
* CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
* DEALINGS IN THE SOFTWARE.
*
*******************************************************************************************************************/
import com.meterware.httpunit.parsing.HTMLParserListener;

/**
 * This interface represents a listener which can receive notification of errors and warnings during the parsing
 * of an HTML page.
 *
 * @deprecated as of 1.5.2, use HTMLParserListener
 *
 * @author <a href="mailto:bx@bigfoot.com">Benoit Xhenseval</a>
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 **/
public interface HtmlErrorListener extends HTMLParserListener {

}
