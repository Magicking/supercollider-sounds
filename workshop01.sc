FreqScope.new
s.plotTree
s.meter
s.scope
s.quit
s.boot
//Ring modulation
Ndef('ringmod', {SinOsc.ar([330, 880])*SinOsc.ar(100);}).play;
Ndef('ringmod', {SinOsc.ar([330, 880])*SinOsc.ar(100);}).play;
//fm modulation
Ndef('fmmoad', {SinOsc.ar(SinOsc.kr(0.2, 0, 1))!2;}).play;
Ndef('sigadd', {
	var env = EnvGen.ar(Env.perc, doneAction: 2);
	var sig = SinOsc.ar(880)*env;
	sig
}).play
SynthDef('synth1', {
	var env = EnvGen.ar(Env.perc, doneAction: 2);
	var sig = SinOsc.ar(880)*env;
	Out.ar(0, sig!2);
}).add
SynthDef('synth2', {
	var env = EnvGen.ar(Env.perc, doneAction: 2);
	var sig = SinOsc.ar(880)*env;
	Out.ar(0, sig!2);
}).add
x = Synth('synth1')
o = OSCresponderNode(s.addr, '/tr', { |time, resp, msg| msg.postln }).add;
SynthDef('synth3', { arg base = 400;
	var env = EnvGen.kr(Env.adsr(0.02, 0.2, 0.25, 1, 1, -4));
//	var env = EnvGen.kr(Env.sine(1), doneAction: 0);
//	var env = EnvGen.kr(Env.xyc([[0.02, 0.02, \sin], [0.5, 1, \sin], [0.8, 1, \sin], [1, 0.02, \sin]]), doneAction: 2);
    var looper = Impulse.kr(1);
    var upper = XLine.kr(1, 10, 2, doneAction: 2);
    var envfreq = EnvGen.kr(Env.xyc([[0, 0, \sin], [0.25, -1, \sin], [0.55, 1, \sin], [0.95, 1, \sin], [1, 0, \sin]]), looper);
    var freq = SinOsc.kr(0.75, mul: 100, add: base - (base / upper));
	var sig = SinOsc.ar(freq*envfreq) * env;
    SendTrig.kr(Impulse.kr(4), 0, upper);
	Out.ar(0, sig!2);
}).play
SynthDef('synth2', { arg base = 400;
	var env = EnvGen.kr(Env.adsr(0.02, 0.2, 0.25, 1, 1, -4));
//	var env = EnvGen.kr(Env.sine(1), doneAction: 0);
//	var env = EnvGen.kr(Env.xyc([[0.02, 0.02, \sin], [0.5, 1, \sin], [0.8, 1, \sin], [1, 0.02, \sin]]), doneAction: 2);
    var looper = Impulse.kr(1);
    var upper = XLine.kr(1, 10, 2, doneAction: 2);
    var envfreq = EnvGen.kr(Env.xyc([[0, 0, \sin], [0.25, -1, \sin], [0.55, 1, \sin], [0.95, 1, \sin], [1, 0, \sin]]), looper);
    var freq = SinOsc.kr(0.75, mul: 100, add: base + (base / upper));
	var sig = SinOsc.ar(freq*envfreq) * env;
    SendTrig.kr(Impulse.kr(4), 0, upper);
	Out.ar(0, sig!2);
}).play
Env.xyc([[0, 0, \sin], [0.25, -1, \sin], [0.55, 1, \sin], [0.95, 1, \sin], [1, 0, \sin]]).test(2).plot;
XLine.kr(1, 10, 10, 0).plot;
s.boot;
s.quit;
(
Pdef(\synthseq,
	Pbind(\instrument,
		Pseq([\synth2, \synth3, \synth3, \synth2], inf))).play
)
Pdef(\synthseq).stop;
SynthDef('kicklow', {
	var env = EnvGen.ar(Env.perc, doneAction: 2);
	var freq = EnvGen.ar(Env([10000, 30], [0.1]));
	var sig = SinOsc.ar(freq)*env;
	Out.ar(0, sig!2);
}).add
SynthDef('kickup', {
	var env = EnvGen.ar(Env.perc, doneAction: 2);
	var freq = EnvGen.ar(Env([30, 10000], [0.1]));
	var sig = SinOsc.ar(freq)*env;
	Out.ar(0, sig!2);
}).add
(
Pdef(\synthseq,
	Pbind(\instrument,
		Pseq([\kicklow, \kicklow, \kickup, \kicklow, \kickup], inf))).play
)
Pdef(\synthseq).stop;

Pdef('kick', Pbind('instrument', 'kick')).play
Pdef('kick').stop;

SynthDef('delay', {
	var in = In.ar(0, 1) + (LocalIn.ar(1)*0.3);
	var sig = DelayL.ar(in, 0.2, 0.2) + in;
	Out.ar(0, sig);
}).add;

SynthDef('delay', {
	var in = In.ar(0,1)+(LocalIn.ar(1)*0.3);
	var sig = DelayL.ar(in, 0.2, 0.2)+in;
	LocalOut.ar(sig);
	Out.ar(0, sig);
}).play;

// Tartini Pitch tracker
