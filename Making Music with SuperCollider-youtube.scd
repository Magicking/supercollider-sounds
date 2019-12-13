// Step 1

SynthDef(\blip, { |freq|
	var env = Env.perc(level:0.1, releaseTime: 0.2).
	kr(doneAction: Done.freeSelf);
	var sig = Pulse.ar(freq: freq, width: 0.05) * env;
	Out.ar(0, [sig, sig])
}).add;

Synth(\blip, [\freq, 660])

// Step 2

Pdef(\melody,
	Pbind(
		\instrument, \blip,
		\dur, Pseq([0.5, 1, 0.5, 0.5, 0.5, 0.5, 0.5], inf),
		\degree, Pseq([0,4,6,8,4,6,7], inf),
		\scale, Scale.minor
	)
).play(TempoClock.default, quant: 4);

// Step 3

SynthDef(\drums, { |freq|
	var env= Env.perc(level: 0.1, releaseTime: 0.2).
	kr(doneAction: Done.freeSelf);
	var sig = WhiteNoise.ar * env;
	var trigger = Impulse.ar*(freq);
	sig = Latch.ar(sig, trigger);
	Out.ar(0, [sig, sig])
}).add;

Synth(\drums, [\freq, 500])
~bass = 500;
~snare = 10000;

// Step 4


Pdef(\melody2,
	Pbind(
		\instrument, \blip,
		\dur, Pseq([0.5, 1, 0.5, 0.5, 0.5, 0.5, 0.5], inf),
		\degree, Pseq([5,9,11,1,8,9,12], inf),
		\scale, Scale.minor
	)
).play(TempoClock.default, quant: 4);
