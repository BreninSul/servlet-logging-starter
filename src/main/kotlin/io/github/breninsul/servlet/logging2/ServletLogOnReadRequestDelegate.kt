/*
 * MIT License
 *
 * Copyright (c) 2024 BreninSul
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.breninsul.servlet.logging2

import io.github.breninsul.servlet.caching.request.ServletCachingRequestWrapper
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.Part
import java.io.BufferedReader
import java.nio.charset.Charset
import java.util.*


open class ServletLogOnReadRequestDelegate(
    open val delegate: ServletCachingRequestWrapper,
    protected open val actionBeforeRead: ServletOnReadAction
) : HttpServletRequest by delegate, ServletLogOnReadRequest {
    fun interface ServletOnReadAction {
        fun performAction(request: HttpServletRequest)
    }

    override fun initRead() {
        delegate.initRead()
    }

    override fun readIsInited(): Boolean {
        return delegate.readIsInited()
    }

    override fun bodyContentByteArray(): ByteArray {
        return delegate.bodyContentByteArray()
    }

    override fun bodyContentString(): String {
        return delegate.bodyContentString()
    }

    override fun reInitInputStream() {
        return delegate.reInitInputStream()
    }

    override fun getContentEncoding(): Charset {
        return delegate.getContentEncoding()
    }

    protected open var performed: Boolean = false
    override fun clear() {
        delegate.clear()
    }

    override fun isPerformed(): Boolean = performed
    override fun performLogActionIfNotPerformedBefore() {
        if (!performed) {
            performed = true
            actionBeforeRead.performAction(this)
        }
    }

    override fun getInputStream(): ServletInputStream {
        performLogActionIfNotPerformedBefore()
        return delegate.inputStream
    }

    override fun getReader(): BufferedReader {
        performLogActionIfNotPerformedBefore()
        return delegate.reader
    }

    override fun getParameter(name: String?): String? {
        performLogActionIfNotPerformedBefore()
        return delegate.getParameter(name)
    }

    override fun getParameterMap(): Map<String, Array<String>> {
        performLogActionIfNotPerformedBefore()
        return delegate.parameterMap
    }

    override fun getParameterNames(): Enumeration<String> {
        performLogActionIfNotPerformedBefore()
        return delegate.parameterNames
    }

    override fun getParameterValues(name: String?): Array<String>? {
        performLogActionIfNotPerformedBefore()
        return delegate.getParameterValues(name)
    }

    override fun getPart(name: String?): Part {
        performLogActionIfNotPerformedBefore()
        return delegate.getPart(name)
    }

    override fun getParts(): MutableCollection<Part> {
        performLogActionIfNotPerformedBefore()
        return delegate.parts
    }

}


