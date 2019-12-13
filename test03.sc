s.boot;
s.quit;
{SinOsc.ar(515)*0.7!2}.play;
{SinOsc.ar(LFNoise0.kr(10).range(500, 1500), mul: 1)!2}.play;
{SinOsc.ar(LFNoise0.kr(10).exprange(500, 1500), mul: 0.7)}.play;
{RLPF.ar(Dust.ar([1, 15]), LFNoise1.ar([0.3, 0.2]).range(100, 3000), 0.02)}.play
FreqScope.new
s.meter;
{Saw.ar(LFNoise0.kr([2, 3]).range(100, 2000), LFPulse.kr([4, 5]) * 0.1)}.play
{LFPulse.ar(10, 0.9)}.play
s.record
s.stopRecording
s.makeWindow
(
  SynthDef("wow", {arg freq = 60, amp = 0.1, gate = 1, wowrelease = 3;
    var chorus, source, filtermod, env, snd;
    chorus = Lag.kr(freq, 2) * LFNoise2.kr([0.4, 0.5, 0.7, 1, 2, 5, 10]).range(1, 1.02);
    source = LFSaw.ar(chorus) * 0.5;
    filtermod = SinOsc.kr(1/16).range(1, 10);
    env = Env.asr(1, amp, wowrelease).kr(2, gate);
    snd = LPF.ar(in: source, freq: freq * filtermod, mul: env);
    Out.ar(0, Splay.ar(snd))
  }).add;
)
a = Array.fill(6, {Synth("wow", ["freq", rrand(40, 70).midicps, "amp", rrand(0.1, 0.5)])});
(
a = Pbind(
    'degree', Pseq([10, -1, 2, -3, 4, -3, 7, 11, 4, 2 ,0 ,-3], 5),
    'dur',    Pseq([0.2, 0.1, 0.1], inf),
    'amp',    Pseq([0.7, 0.5, 0.3, 0.2], inf),
    'legato', 0.4
).play
)
(
var dur_n = 0.2;
var dur_last =  0.5;
a = Pbind(
    'freq', Pwhite(100, 500),
    'dur',  Prand([1, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3], 9),
).play
)
(
a = Pbind(
    'instrument', 'wow',
    'note', Pseq([0, 5, 8], 3),
    'dur', Pgeom(1.5, 0.9, 3),
    'legato', 1

).trace.play
)
(
Pbind(
    'note', Pseq([0, 5, 8], 3),
    'dur', 0.15
).play;
)
a = Pbind(\degree, 0).play;
a.stop;
Pbind(\degree, 0).stop
s.boot
s.quit
(
a = Pbind(
    'note', Pseq([[0, 3, 7], [2, 5, 8], [3, 7, 10], [5, 8, 12]], 3),
    'dur', 0.15,
).play;
)
s.quit;
s.kill
MIDIClient.disposeClient();
(
var notes, on, off, controller, cc;

MIDIClient.disposeClient();
MIDIClient.init(1, 1, true);
controller = MIDIIn.findPort("Joystick0", "Joystick Output");
MIDIIn.connect(0, controller);
MIDIFunc.trace(false);

notes = Array.newClear(128);    // array has one slot per possible MIDI note

on = MIDIFunc.noteOn({ |veloc, num, chan, src|
    "test".postln;
    notes[num] = Synth('default', ['freq', num.midicps,
            'amp', veloc * 0.00315]);
});

off = MIDIFunc.noteOff({ |veloc, num, chan, src|
    "test2".postln;
    notes[num].release;
});

/*
cc = MIDIFunc.cc({ |num, chan, srcID| 
    num.postln;
    chan.postln;
    srcID.postln;
});
*/
q = { on.free; off.free; cc.free; };
)

// when done:
q.value;

(
SynthDef.new(\sineMIDI, {
	arg freq=440;
	var amp, sig;
	amp = LFNoise1.kr(12).exprange(0.02,1);
	sig = SinOsc.ar(freq) * amp;
	Out.ar(0, sig);
}).add;
)
(
MIDIClient.disposeClient();
MIDIClient.init(1, 1, true);
MIDIIn.connect(0, MIDIIn.findPort("Joystick0", "Joystick Output"));
MIDIFunc.trace(false);
)
x = Synth.new(\sineMIDI);
x.free;
q.value;
(
var fader0, fader1, fader2, fader3, cc;
cc = MIDIdef.cc('ccTest', {
	arg vel, ccNum, chan, srcID;

	[vel, ccNum, chan, srcID];
	(
	switch(ccNum,
		14, {fader0=vel.linlin(0, 127, 220, 880)},
		13, {fader1=vel},
		12, {fader2=vel},
		11, {fader3=vel}
	);
	);
	[fader0, fader1, fader2, fader3].postln;
	x.set('freq', fader0)
});
q = { cc.free; };
)

