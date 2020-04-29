/*
 * Copyright 2018–2020 Android Charts Open Source Project
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

package it.czerwinski.android.charts.core

import android.graphics.Canvas
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CanvasTest {

    @MockK
    lateinit var paint: TextPaint

    @MockK(relaxUnitFun = true)
    lateinit var canvas: Canvas

    @Test
    fun `Given text paint without all caps, when drawTextAdvanced, then draw the original text`() {
        every { paint.textAllCaps } returns false

        canvas.drawTextAdvanced(text = "Some text", x = 1f, y = 2f, paint = paint)

        verify(exactly = 1) { canvas.drawText("Some text", 1f, 2f, paint) }
    }

    @Test
    fun `Given text paint with all caps, when drawTextAdvanced, then draw text in all caps`() {
        every { paint.textAllCaps } returns true

        canvas.drawTextAdvanced(text = "Some text", x = 1f, y = 2f, paint = paint)

        verify(exactly = 1) { canvas.drawText("SOME TEXT", 1f, 2f, paint) }
    }
}
