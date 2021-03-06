/*
 * Copyright 2016-2017 the original author or authors from the JHipster project.
 *
 * This file is part of the JHipster project, see http://www.jhipster.tech/
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.jhipster.config.metrics;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.*;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.writer.Delta;

import io.github.jhipster.test.LogbackRecorder;
import io.github.jhipster.test.LogbackRecorder.Event;

public class SpectatorLogMetricWriterTest {

    private LogbackRecorder recorder;
    private SpectatorLogMetricWriter writer;

    @Before
    public void setup() {
        writer = new SpectatorLogMetricWriter();
        recorder = LogbackRecorder.forName("metrics").reset().capture("ALL");
    }

    @After
    public void teardown() {
        writer.reset("*");
        recorder.release();
    }

    @Test
    public void testSetRibbonCommand() {
        Metric<Double> metric = new Metric<>("hystrix.HystrixCommand.RibbonCommand.foo.bar", 42D);

        writer.set(metric);

        List<Event> events = recorder.play();
        assertThat(events).hasSize(1);
        Event event = events.get(0);
        assertThat(event.getMessage()).isEqualTo(SpectatorLogMetricWriter.SET_MESSAGE);
        Object[] args = event.getArguments();
        assertThat(args).hasSize(5);
        assertThat(args[0]).isEqualTo("hystrix.HystrixCommand.RibbonCommand");
        assertThat(args[1]).isEqualTo("foo");
        assertThat(args[2]).isEqualTo("");
        assertThat(args[3]).isEqualTo("foo.bar");
        assertThat(args[4]).isEqualTo(42D);
    }

    @Test
    public void testSetHystrixCommand() {
        Metric<Double> metric = new Metric<>("hystrix.HystrixCommand.foo.bar", 42D);

        writer.set(metric);

        List<Event> events = recorder.play();
        assertThat(events).hasSize(1);
        Event event = events.get(0);
        assertThat(event.getMessage()).isEqualTo(SpectatorLogMetricWriter.SET_MESSAGE);
        Object[] args = event.getArguments();
        assertThat(args).hasSize(5);
        assertThat(args[0]).isEqualTo("hystrix.HystrixCommand");
        assertThat(args[1]).isEqualTo("foo");
        assertThat(args[2]).isEqualTo("bar");
        assertThat(args[3]).isEqualTo("foo.bar");
        assertThat(args[4]).isEqualTo(42D);
    }

    @Test
    public void testSetHystrixThreadPool() {
        Metric<Double> metric = new Metric<>("hystrix.HystrixThreadPool.foo.bar", 42D);

        writer.set(metric);

        List<Event> events = recorder.play();
        assertThat(events).hasSize(1);
        Event event = events.get(0);
        assertThat(event.getMessage()).isEqualTo(SpectatorLogMetricWriter.SET_MESSAGE);
        Object[] args = event.getArguments();
        assertThat(args).hasSize(5);
        assertThat(args[0]).isEqualTo("hystrix.HystrixThreadPool");
        assertThat(args[1]).isEqualTo("foo");
        assertThat(args[2]).isEqualTo("bar");
        assertThat(args[3]).isEqualTo("foo.bar");
        assertThat(args[4]).isEqualTo(42D);
    }

    @Test
    public void testIncrement() {
        Delta<Double> delta = new Delta<>("foo.bar", 42D);

        writer.increment(delta);

        List<Event> events = recorder.play();
        assertThat(events).hasSize(1);
        Event event = events.get(0);
        assertThat(event.getMessage()).isEqualTo(SpectatorLogMetricWriter.INCREMENT_MESSAGE);
        Object[] args = event.getArguments();
        assertThat(args).hasSize(2);
        assertThat(args[0]).isEqualTo("foo.bar");
        assertThat(args[1]).isEqualTo(42D);
    }
}
